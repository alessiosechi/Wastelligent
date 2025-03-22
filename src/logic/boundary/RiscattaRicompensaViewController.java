package logic.boundary;

import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import logic.beans.RicompensaBean;
import logic.beans.RiscattoBean;
import logic.boundary.components.ViewInfo;
import logic.boundary.components.ViewLoader;
import logic.controller.RiscattaRicompensaController;
import logic.exceptions.ConnessioneAPIException;
import logic.exceptions.DailyRedemptionLimitException;
import logic.exceptions.GestioneRiscattoException;
import logic.exceptions.InsufficientPointsException;
import logic.observer.Observer;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Label;

public class RiscattaRicompensaViewController implements Observer {

	@FXML
	private TableView<RiscattoBean> tableViewRiscatti;

	@FXML
	private TableColumn<RiscattoBean, String> colNomeRicompensa;
	@FXML
	private TableColumn<RiscattoBean, String> colCodiceRiscatto;
	@FXML
	private TableColumn<RiscattoBean, String> colDescrizione;
	@FXML
	private TableColumn<RiscattoBean, String> colValore;
	@FXML
	private TableColumn<RiscattoBean, String> colDataRiscatto;
	@FXML
	private TableColumn<RiscattoBean, String> colDataScadenza;
	@FXML
	private TableColumn<RiscattoBean, Integer> colPuntiUtilizzati;

	@FXML
	private Label dettagliRicompensaLabel;
	@FXML
	private Label labelSaldoPunti;
	@FXML
	private ComboBox<String> comboBoxRicompense;
	@FXML
	private Button btnRiscatta;
	@FXML
	private Button exitButton;
	@FXML
	private Button nuovaSegnalazioneButton;
	@FXML
	private Button visualizzaStoricoButton;

	private RiscattaRicompensaController riscattaRicompensaController = new RiscattaRicompensaController();
	private List<RicompensaBean> listaRicompenseAPI;

	@FXML
	public void initialize() {
		riscattaRicompensaController.registraOsservatoreRiscatti(this);
		caricaPuntiUtente();
		mostraRicompenseDisponibili();
		configuraEventi();
		configuraColonneTableView();

		tableViewRiscatti.setItems(FXCollections.observableArrayList(getRiscatti()));

		riscattaRicompensaController.registraOsservatoreRiscatti(this);
	}

	private void caricaPuntiUtente() {
		int puntiUtente = riscattaRicompensaController.ottieniPuntiUtente();
		labelSaldoPunti.setText(String.valueOf(puntiUtente));
	}

	private void mostraRicompenseDisponibili() {
		// carico le ricompense disponibili solo una volta
		if (listaRicompenseAPI == null) {
			try {
				listaRicompenseAPI = riscattaRicompensaController.ottieniRicompenseAPI();
			} catch (ConnessioneAPIException e) {
				showAlert("Errore di connessione", e.getMessage());
			}
		}

		ObservableList<String> ricompense = FXCollections.observableArrayList();
		listaRicompenseAPI.forEach(r -> ricompense.add(r.getNome()));
		comboBoxRicompense.setItems(ricompense);
	}

	private void configuraEventi() {
		nuovaSegnalazioneButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.EFFETTUA_SEGNALAZIONE_VIEW));
		exitButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.LOGIN_VIEW));
		visualizzaStoricoButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.STORICO_VIEW));
		btnRiscatta.setOnAction(event -> riscattaSelezione());

		comboBoxRicompense.setOnAction(event -> {
			int selectedIndex = comboBoxRicompense.getSelectionModel().getSelectedIndex();
			if (selectedIndex >= 0) {
				RicompensaBean ricompensaSelezionata = listaRicompenseAPI.get(selectedIndex);
				mostraDettagliRicompensa(ricompensaSelezionata); // mostro i dettagli nella label
			}
		});
	}

	private void configuraColonneTableView() {

		colNomeRicompensa
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomeRicompensa()));
		colCodiceRiscatto
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodiceRiscatto()));
		colDescrizione.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getDescrizioneRicompensa()));
		colValore.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getValoreRicompensa() + " €"));

		colDataRiscatto
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataRiscatto()));
		colDataScadenza.setCellValueFactory(
				cellData -> new SimpleStringProperty(cellData.getValue().getDataScadenzaRicompensa()));
		colPuntiUtilizzati
				.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPunti()).asObject());
	}

	private List<RiscattoBean> getRiscatti() {
		return riscattaRicompensaController.ottieniRiscattiUtente();
	}

	private void mostraDettagliRicompensa(RicompensaBean ricompensaBean) {
		int puntiNecessari = riscattaRicompensaController.calcolaPuntiNecessari(ricompensaBean.getValore());
		String dettagli = String.format(
				"Nome: %s%nValore: %d€%nDescrizione: %s%nData scadenza: %s%nPunti necessari: %d",
				ricompensaBean.getNome(), ricompensaBean.getValore(), ricompensaBean.getDescrizione(),
				ricompensaBean.getDataScadenza(), puntiNecessari);
		dettagliRicompensaLabel.setText(dettagli);
	}

	private void riscattaSelezione() {
		int selectedIndex = comboBoxRicompense.getSelectionModel().getSelectedIndex();

		if (selectedIndex < 0) {
			showAlert("Nessuna Ricompensa Selezionata", "Seleziona una ricompensa.");
			return;
		}

		RicompensaBean ricompensaBean = listaRicompenseAPI.get(selectedIndex);

		try {
			boolean result = riscattaRicompensaController.riscatta(ricompensaBean);

			String message = result ? "Ricompensa riscattata con successo!"
					: "Impossibile riscattare la ricompensa. Riprova.";
			showAlert(result ? "Successo" : "Errore", message);

		} catch (DailyRedemptionLimitException e) {

			showAlert("Limite riscatti giornalieri raggiunto", e.getMessage());

		} catch (InsufficientPointsException e) {

			showAlert("Punti insufficienti", e.getMessage());

		} catch (GestioneRiscattoException e) {

			showAlert("Errore nel recupero del codice di riscatto", e.getMessage());

		} catch (Exception e) {
			showAlert("Errore", "Si è verificato un errore imprevisto. Riprova più tardi.");
		}
	}

	private void showAlert(String titolo, String messaggio) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(titolo);
		alert.setHeaderText(null);
		alert.setContentText(messaggio);
		alert.showAndWait();
	}

	@Override
	public void update() {
		tableViewRiscatti.setItems(FXCollections.observableArrayList(riscattaRicompensaController.getRiscatti()));
		labelSaldoPunti.setText(String.valueOf(riscattaRicompensaController.ottieniPuntiUtente()));
	}

}

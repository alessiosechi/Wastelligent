package logic.boundary;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import logic.beans.OperatoreEcologicoBean;
import logic.beans.SegnalazioneBean;
import logic.boundary.components.ViewInfo;
import logic.boundary.components.ViewLoader;
import logic.controller.RisolviSegnalazioneController;
import logic.exceptions.OperatoreNonDisponibileException;
import logic.observer.Observer;

public class GestisciSegnalazioniViewController implements Observer {

	@FXML
	private Button exitButton;
	@FXML
	private Button assegnaButton;
	@FXML
	private Button assegnaPuntiButton;
	@FXML
	private Button vediDettagliButton;
	@FXML
	private Button eliminaButton;

	@FXML
	private TableView<SegnalazioneBean> segnalazioniTable;
	@FXML
	private TableColumn<SegnalazioneBean, Integer> idColumn;
	@FXML
	private TableColumn<SegnalazioneBean, String> descrizioneColumn;
	@FXML
	private TableColumn<SegnalazioneBean, String> posizioneColumn;
	@FXML
	private TableColumn<SegnalazioneBean, String> statoColumn;
	@FXML
	private ComboBox<String> operatoriEcologiciComboBox;

	private DettagliSegnalazioneViewController dettagliSegnalazioneViewController = DettagliSegnalazioneViewController
			.getInstance();
	private RisolviSegnalazioneController risolviSegnalazioneController = RisolviSegnalazioneController.getInstance();
	private static final Logger logger = Logger.getLogger(GestisciSegnalazioniViewController.class.getName());
	private List<OperatoreEcologicoBean> operatoriEcologici;

	private void configuraPulsanti() {
		exitButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.LOGIN_VIEW));
		vediDettagliButton.setDisable(true);
		eliminaButton.setDisable(true);
		assegnaButton.setDisable(true);
	}

	private void configuraColonne() {
		idColumn.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(cellData.getValue().getIdSegnalazione()).asObject());
		descrizioneColumn
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescrizione()));
		posizioneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosizione()));
		statoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStato()));
	}

	private void configuraSelezioneTabella() {
		segnalazioniTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> gestisciSelezioneTabella(newValue));
	}

	private void gestisciSelezioneTabella(SegnalazioneBean newValue) {
		if (newValue != null) {
			vediDettagliButton.setDisable(false);
			eliminaButton.setDisable(false);
			assegnaButton.setDisable(false);

			dettagliSegnalazioneViewController.setSegnalazioneBean(newValue);
			dettagliSegnalazioneViewController.setCallerType(CallerType.CONTROLLER2);
		} else {
			vediDettagliButton.setDisable(true);
			eliminaButton.setDisable(true);
			assegnaButton.setDisable(true);
		}
	}

	private void configuraAzioniPulsanti() {
		vediDettagliButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.DETTAGLI_VIEW));
		eliminaButton.setOnAction(event -> gestisciEliminazione());
		assegnaButton.setOnAction(event -> gestisciAssegnazione());
		assegnaPuntiButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.ASSEGNA_PUNTI_VIEW));
	}

	private void gestisciEliminazione() {
		Optional<ButtonType> result = showAlert("Conferma Eliminazione",
				"Sei sicuro di voler eliminare la segnalazione selezionata?", Alert.AlertType.CONFIRMATION);
		if (result.isPresent() && result.get() == ButtonType.OK) {
			SegnalazioneBean segnalazioneSelezionata = segnalazioniTable.getSelectionModel().getSelectedItem();
			risolviSegnalazioneController.eliminaSegnalazione(segnalazioneSelezionata);
		} else {
			logger.info("Eliminazione annullata.");
		}
	}

	private void gestisciAssegnazione() {
		int selectedIndex = operatoriEcologiciComboBox.getSelectionModel().getSelectedIndex();
		OperatoreEcologicoBean operatoreSelezionato;
		if (selectedIndex != -1) {
			operatoreSelezionato = operatoriEcologici.get(selectedIndex);
		} else {
			operatoreSelezionato = null;
		}

		SegnalazioneBean segnalazioneSelezionata = segnalazioniTable.getSelectionModel().getSelectedItem();

		if (operatoreSelezionato != null) {
			try {
				if (risolviSegnalazioneController.assegnaOperatore(segnalazioneSelezionata, operatoreSelezionato)) {
					logger.info("Segnalazione assegnata con successo a " + operatoreSelezionato.getUsername());
				} else {
					showAlert("Errore Assegnazione",
							"Si è verificato un errore durante l'assegnazione della segnalazione.",
							Alert.AlertType.INFORMATION);
				}
			} catch (OperatoreNonDisponibileException e) {
				showAlert("Operatore non disponibile", e.getMessage(), Alert.AlertType.INFORMATION);
			}
		} else {
			showAlert("Selezione mancante", "Seleziona l'operatore per procedere con l'assegnazione.",
					Alert.AlertType.WARNING);
		}
	}

	public void initialize() {
		configuraPulsanti();
		configuraColonne();
		caricaSegnalazioniDaRisolvere();
		caricaOperatoriEcologici();
		configuraSelezioneTabella();
		configuraAzioniPulsanti();

		risolviSegnalazioneController.registraOsservatoreSegnalazioniAttive(this);
	}

	private void caricaSegnalazioniDaRisolvere() {

		List<SegnalazioneBean> segnalazioniDaRisolvere = risolviSegnalazioneController.getSegnalazioniRicevute();

		ObservableList<SegnalazioneBean> segnalazioniData = FXCollections.observableArrayList(segnalazioniDaRisolvere);
		segnalazioniTable.setItems(segnalazioniData);
	}

	private void caricaOperatoriEcologici() {

		// ottengo la lista di operatori dal controller applicativo
		operatoriEcologici = risolviSegnalazioneController.getOperatoriEcologiciDisponibili();

		ObservableList<String> operatori = FXCollections.observableArrayList();
		operatoriEcologici.forEach(o -> operatori.add(o.getUsername()));
		operatoriEcologiciComboBox.setItems(operatori);
	}

	private Optional<ButtonType> showAlert(String title, String message, Alert.AlertType alertType) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);

		return alert.showAndWait();
	}

	@Override
	public void update() {
		List<SegnalazioneBean> segnalazioniDaRisolvere = risolviSegnalazioneController.getSegnalazioniAttive();
		ObservableList<SegnalazioneBean> segnalazioniData = FXCollections.observableArrayList(segnalazioniDaRisolvere);
		segnalazioniTable.setItems(segnalazioniData);
	}

}

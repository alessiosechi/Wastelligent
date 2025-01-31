package logic.boundary2;

import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import logic.beans.SegnalazioneBean;
import logic.controller.AssegnaPuntiController;
import logic.observer.Observer;

public class ValutaSegnalazioniViewController implements Observer {

	@FXML
	private BorderPane rootPane;
	@FXML
	private TableView<SegnalazioneBean> tableViewSegnalazioni;
	@FXML
	private TableColumn<SegnalazioneBean, Integer> colIdSegnalazione;
	@FXML
	private TableColumn<SegnalazioneBean, String> colDescrizione;
	@FXML
	private TableColumn<SegnalazioneBean, String> colPosizione;

	@FXML
	private Button fotoButton;
	@FXML
	private Button posizioneButton;
	@FXML
	private Button assegnaButton;

	@FXML
	private Slider puntiSlider;
	@FXML
	private Label puntiLabel;

	private SegnalazioneBean segnalazioneSelezionata = null;
	private AssegnaPuntiController assegnaPuntiController = AssegnaPuntiController.getInstance();

	@FXML
	public void initialize() {
		AnchorPane sidebar = SidebarLoader.caricaSidebar(SidebarType.ESPERTO_ECOLOGICO_SIDEBAR);
		rootPane.setLeft(sidebar);

		configuraColonneTabella();
		configuraSlider();
		configuraPulsanti();
		caricaSegnalazioni();
		assegnaPuntiController.registraOsservatoreSegnalazioniRisolte(this);

		tableViewSegnalazioni.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {
					segnalazioneSelezionata = newValue;
					aggiornaPulsanti();
				});
	}

	private void configuraColonneTabella() {
		colIdSegnalazione.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(cellData.getValue().getIdSegnalazione()).asObject());
		colDescrizione.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescrizione()));
		colPosizione.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosizione()));
	}

	private void configuraSlider() {
		// aggiorno il valore della label quando lo slider cambia
		puntiSlider.valueProperty().addListener(
				(observable, oldValue, newValue) -> puntiLabel.setText(String.valueOf(newValue.intValue())));
	}

	private void configuraPulsanti() {
		fotoButton.setDisable(true);
		posizioneButton.setDisable(true);
		fotoButton.setOnAction(event -> handleFotoAction());
		posizioneButton.setOnAction(event -> handlePosizioneAction());
		assegnaButton.setOnAction(event -> assegnaPuntiSegnalazione());
	}

	private void caricaSegnalazioni() {
		List<SegnalazioneBean> segnalazioniRisolte = assegnaPuntiController.getSegnalazioniDaRiscontrare();
		ObservableList<SegnalazioneBean> segnalazioni = FXCollections.observableArrayList(segnalazioniRisolte);
		tableViewSegnalazioni.setItems(segnalazioni);
	}

	private void aggiornaPulsanti() {
		fotoButton.setDisable(false);
		posizioneButton.setDisable(false);
		assegnaButton.setDisable(false);
	}

	private void handleFotoAction() {
		DettagliSegnalazione.mostraFoto(segnalazioneSelezionata.getFoto());
	}

	private void handlePosizioneAction() {
		DettagliSegnalazione.mostraMappa(segnalazioneSelezionata.getLatitudine(),
				segnalazioneSelezionata.getLongitudine());
	}

	private void assegnaPuntiSegnalazione() {
		if (segnalazioneSelezionata != null) {
			int punti = (int) puntiSlider.getValue();
			segnalazioneSelezionata.setPuntiAssegnati(punti);

			boolean successo = assegnaPuntiController.assegnaPunti(segnalazioneSelezionata);

			if (successo) {
				showAlert("Successo", "Punti assegnati con successo.");
			} else {
				showAlert("Errore", "Si è verificato un errore durante l'assegnazione dei punti.");
			}
		} else {
			showAlert("Errore", "Seleziona una segnalazione.");
		}
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	@Override
	public void update() {
		List<SegnalazioneBean> segnalazioniRisolte = assegnaPuntiController.getSegnalazioniRisolte();
		ObservableList<SegnalazioneBean> segnalazioni = FXCollections.observableArrayList(segnalazioniRisolte);
		tableViewSegnalazioni.setItems(segnalazioni);
	}
}

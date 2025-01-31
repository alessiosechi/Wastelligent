package logic.boundary;

import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import logic.beans.SegnalazioneBean;
import logic.boundary.components.ViewInfo;
import logic.boundary.components.ViewLoader;
import logic.controller.AssegnaPuntiController;
import logic.observer.Observer;

public class AssegnaPuntiViewController implements Observer {

	@FXML
	private Button exitButton;
	@FXML
	private Button gestisciSegnalazioniButton;
	@FXML
	private Button dettagliButton;
	@FXML
	private Button assegnaButton;
	@FXML
	private TextField puntiTextField;

	@FXML
	private TableView<SegnalazioneBean> segnalazioniTable;
	@FXML
	private TableColumn<SegnalazioneBean, Integer> idColumn;
	@FXML
	private TableColumn<SegnalazioneBean, String> descrizioneColumn;
	@FXML
	private TableColumn<SegnalazioneBean, String> posizioneColumn;

	private AssegnaPuntiController assegnaPuntiController = AssegnaPuntiController.getInstance();
	private DettagliSegnalazioneViewController dettagliSegnalazioneViewController = DettagliSegnalazioneViewController
			.getInstance();

	@FXML
	public void initialize() {
		String valoreInizialeTextField = puntiTextField.getText();
		caricaSegnalazioniRisolte();
		configuraColonneTabella();
		configuraPulsanti();
		impostaListenerSelezione(valoreInizialeTextField);

		assegnaPuntiController.registraOsservatoreSegnalazioniRisolte(this);
	}

	private void configuraColonneTabella() {
		idColumn.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(cellData.getValue().getIdSegnalazione()).asObject());
		descrizioneColumn
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescrizione()));
		posizioneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosizione()));
	}

	private void configuraPulsanti() {
		dettagliButton.setDisable(true);
		assegnaButton.setDisable(true);
		dettagliButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.DETTAGLI_VIEW));
		assegnaButton.setOnAction(event -> assegnaPuntiSegnalazione());
		exitButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.LOGIN_VIEW));
		gestisciSegnalazioniButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.GESTISCI_SEGNALAZIONI_VIEW));
	}

	private void assegnaPuntiSegnalazione() {
		SegnalazioneBean segnalazioneSelezionata = segnalazioniTable.getSelectionModel().getSelectedItem();
		String inputTextField = puntiTextField.getText();

		if (inputTextField.isEmpty()) {
			showAlert("Errore", "Inserisci i punti da assegnare.");
			return;
		}

		try {

			int punti = Integer.parseInt(inputTextField);
			segnalazioneSelezionata.setPuntiAssegnati(punti);
		} catch (NumberFormatException e) {

			showAlert("Errore", "Inserisci un numero intero valido.");
		}
		
		boolean successo = assegnaPuntiController.assegnaPunti(segnalazioneSelezionata);

		if (!successo) {
			showAlert("Errore Assegnazione", "Si è verificato un errore durante l'assegnazione dei punti.");
		}

	}

	private void impostaListenerSelezione(String valoreInizialeTextField) {
		segnalazioniTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				dettagliButton.setDisable(false);
				assegnaButton.setDisable(false);

				dettagliSegnalazioneViewController.setSegnalazioneBean(newValue);
				dettagliSegnalazioneViewController.setCallerType(CallerType.CONTROLLER1);
			} else {
				dettagliButton.setDisable(true);
				assegnaButton.setDisable(true);
				puntiTextField.setText(valoreInizialeTextField);
			}
		});
	}

	private void caricaSegnalazioniRisolte() {
		List<SegnalazioneBean> segnalazioniRisolte = assegnaPuntiController.getSegnalazioniDaRiscontrare();

		ObservableList<SegnalazioneBean> segnalazioni = FXCollections.observableArrayList(segnalazioniRisolte);
		segnalazioniTable.setItems(segnalazioni);
	}

	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	@Override
	public void update() {
		List<SegnalazioneBean> segnalazioniRisolte = assegnaPuntiController.getSegnalazioniRisolte();

		ObservableList<SegnalazioneBean> segnalazioni = FXCollections.observableArrayList(segnalazioniRisolte);
		segnalazioniTable.setItems(segnalazioni);
	}

}

package logic.boundary;

import java.util.List;
import java.util.logging.Logger;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import logic.beans.SegnalazioneBean;
import logic.boundary.components.ViewInfo;
import logic.boundary.components.ViewLoader;
import logic.controller.RisolviSegnalazioneController;
import logic.observer.Observer;

public class SegnalazioniAssegnateViewController implements Observer {

	@FXML
	private Button exitButton;
	@FXML
	private Button dettagliButton;
	@FXML
	private Button completaButton;

	@FXML
	private TableView<SegnalazioneBean> segnalazioniTable;
	@FXML
	private TableColumn<SegnalazioneBean, Integer> idColumn;
	@FXML
	private TableColumn<SegnalazioneBean, String> descrizioneColumn;
	@FXML
	private TableColumn<SegnalazioneBean, String> posizioneColumn;

	private RisolviSegnalazioneController risolviSegnalazioneController = RisolviSegnalazioneController.getInstance();
	private static final Logger logger = Logger.getLogger(SegnalazioniAssegnateViewController.class.getName());

	@FXML
	public void initialize() {
		configuraPulsanti();
		configuraColonneTabella();
		configuraSelezioneTabella();
		configuraEventiPulsanti();
		caricaAssegnazioni();

		risolviSegnalazioneController.registraOsservatoreSegnalazioniAssegnate(this);

	}

	private void configuraPulsanti() {
		exitButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.LOGIN_VIEW));
		dettagliButton.setDisable(true);
	}

	private void configuraColonneTabella() {
		idColumn.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(cellData.getValue().getIdSegnalazione()).asObject());
		descrizioneColumn
				.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescrizione()));
		posizioneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosizione()));
	}

	private void configuraSelezioneTabella() {
		segnalazioniTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
		    dettagliButton.setDisable(newValue == null);
		});
	}

	private void configuraEventiPulsanti() {
		dettagliButton.setOnAction(event -> ViewLoader.caricaDettagliSegnalazioneView(
				segnalazioniTable.getSelectionModel().getSelectedItem(), CallerType.CONTROLLER3));

		completaButton.setOnAction(event -> {
			SegnalazioneBean segnalazione = segnalazioniTable.getSelectionModel().getSelectedItem();
			if (segnalazione != null) {
				boolean successo = risolviSegnalazioneController
						.completaSegnalazione(segnalazioniTable.getSelectionModel().getSelectedItem());
				if (successo) {
					logger.info("Segnalazione completata!");
				} else {
					showAlert("Errore Completamento",
							"Si è verificato un errore durante il completamento della segnalazione.");
				}
			} else {
				showAlert("Selezione Mancante", "Seleziona una segnalazione.");
			}
		});
	}

	private void caricaAssegnazioni() {

		List<SegnalazioneBean> segnalazioniAssegnate = risolviSegnalazioneController.getSegnalazioniDaRisolvere();

		ObservableList<SegnalazioneBean> segnalazioni = FXCollections.observableArrayList(segnalazioniAssegnate);
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
		List<SegnalazioneBean> segnalazioniAssegnate = risolviSegnalazioneController.getSegnalazioniAssegnate();

		ObservableList<SegnalazioneBean> segnalazioni = FXCollections.observableArrayList(segnalazioniAssegnate);
		segnalazioniTable.setItems(segnalazioni);
	}

}

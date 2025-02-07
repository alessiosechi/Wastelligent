package logic.boundary2;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import logic.beans.OperatoreEcologicoBean;
import logic.beans.SegnalazioneBean;
import logic.controller.RisolviSegnalazioneController;
import logic.exceptions.OperatoreNonDisponibileException;
import logic.observer.Observer;

public class AssegnaSegnalazioniViewController implements Observer {
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
	private TableColumn<SegnalazioneBean, String> colStato;

	@FXML
	private Button fotoButton;
	@FXML
	private Button posizioneButton;
	@FXML
	private Button assegnaButton;
	@FXML
	private Button eliminaButton;
	@FXML
	private ComboBox<String> operatoriComboBox;

	private RisolviSegnalazioneController risolviSegnalazioneController = RisolviSegnalazioneController.getInstance();
	private List<OperatoreEcologicoBean> operatoriEcologici;
	private static final Logger logger = Logger.getLogger(AssegnaSegnalazioniViewController.class.getName());
	private SegnalazioneBean segnalazioneSelezionata = null;

	@FXML
	public void initialize() {

		AnchorPane sidebar = SidebarLoader.caricaSidebar(SidebarType.ESPERTO_ECOLOGICO_SIDEBAR);
		rootPane.setLeft(sidebar);

		configuraColonne();
		configuraSelezioneTabella();
		configuraPulsanti();
		caricaSegnalazioniDaRisolvere();
		caricaOperatoriEcologici();
		risolviSegnalazioneController.registraOsservatoreSegnalazioniAttive(this);

	}

	private void configuraColonne() {
		colIdSegnalazione.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(cellData.getValue().getIdSegnalazione()).asObject());
		colDescrizione.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescrizione()));
		colPosizione.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosizione()));
		colStato.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStato()));
	}

	private void configuraSelezioneTabella() {
		tableViewSegnalazioni.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {
					segnalazioneSelezionata = newValue;
					gestisciSelezioneTabella();
				});
	}

	private void gestisciSelezioneTabella() {
		boolean disabled = segnalazioneSelezionata == null;
		fotoButton.setDisable(disabled);
		posizioneButton.setDisable(disabled);
		eliminaButton.setDisable(disabled);
		assegnaButton.setDisable(disabled);
	}

	private void configuraPulsanti() {
		fotoButton.setDisable(true);
		posizioneButton.setDisable(true);
		eliminaButton.setDisable(true);
		assegnaButton.setDisable(true);

		eliminaButton.setOnAction(event -> gestisciEliminazione());
		assegnaButton.setOnAction(event -> gestisciAssegnazione());

		fotoButton.setOnAction(event -> {
			if (segnalazioneSelezionata != null) {
				DettagliSegnalazione.mostraFoto(segnalazioneSelezionata.getFoto());
			} else {
				showAlert(Alert.AlertType.WARNING, "Avviso", "Seleziona prima una segnalazione.", false);
			}
		});

		posizioneButton.setOnAction(event -> {
			if (segnalazioneSelezionata != null) {
				DettagliSegnalazione.mostraMappa(segnalazioneSelezionata.getLatitudine(),
						segnalazioneSelezionata.getLongitudine());
			} else {
				showAlert(Alert.AlertType.WARNING, "Avviso", "Seleziona prima una segnalazione.", false);
			}
		});
	}

	private void caricaSegnalazioniDaRisolvere() {
		ObservableList<SegnalazioneBean> segnalazioniData = FXCollections
				.observableArrayList(risolviSegnalazioneController.getSegnalazioniRicevute());
		tableViewSegnalazioni.setItems(segnalazioniData);
	}

	private void caricaOperatoriEcologici() {
	    operatoriEcologici = risolviSegnalazioneController.getOperatoriEcologiciDisponibili();
	    operatoriComboBox.setItems(FXCollections.observableArrayList(
	        operatoriEcologici.stream().map(OperatoreEcologicoBean::getUsername).toList()));
	     
	    // per ogni oggetto OperatoreEcologicoBean, estraggo il valore del suo campo username
	}


	private void gestisciEliminazione() {
		Optional<ButtonType> result = showAlert(Alert.AlertType.CONFIRMATION, "Conferma Eliminazione",
				"Sei sicuro di voler eliminare la segnalazione selezionata?", true);

		result.filter(buttonType -> buttonType == ButtonType.OK)
				.ifPresent(buttonType -> Optional.ofNullable(segnalazioneSelezionata).ifPresentOrElse(
						s -> risolviSegnalazioneController.eliminaSegnalazione(s), // s è la segnalazione selezionata
						() -> logger.info("Nessuna segnalazione selezionata."))); // con () non prendo input
	}

	private void gestisciAssegnazione() {
		int selectedIndex = operatoriComboBox.getSelectionModel().getSelectedIndex();
		OperatoreEcologicoBean operatoreSelezionato = (selectedIndex != -1) ? operatoriEcologici.get(selectedIndex)
				: null;

		SegnalazioneBean segnalazione = tableViewSegnalazioni.getSelectionModel().getSelectedItem();

		if (operatoreSelezionato != null) {

			try {
				if (risolviSegnalazioneController.assegnaOperatore(segnalazione, operatoreSelezionato)) {
					logger.info("Segnalazione assegnata con successo a " + operatoreSelezionato.getUsername());
				} else {
					showAlert(Alert.AlertType.ERROR, "Errore Assegnazione",
							"Si è verificato un errore durante l'assegnazione.", false);
				}
			} catch (OperatoreNonDisponibileException e) {
				showAlert(Alert.AlertType.WARNING, "Operatore non disponibile", e.getMessage(), true);
			}
		} else {
			showAlert(Alert.AlertType.WARNING, "Errore", "Seleziona un operatore per procedere.", true);
		}

	}

	private Optional<ButtonType> showAlert(Alert.AlertType alertType, String title, String message,
			boolean waitForResponse) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		return waitForResponse ? alert.showAndWait() : Optional.empty();
	}

	@Override
	public void update() {
		List<SegnalazioneBean> segnalazioniDaRisolvere = risolviSegnalazioneController.getSegnalazioniAttive();
		ObservableList<SegnalazioneBean> segnalazioniData = FXCollections.observableArrayList(segnalazioniDaRisolvere);
		tableViewSegnalazioni.setItems(segnalazioniData);
	}
}

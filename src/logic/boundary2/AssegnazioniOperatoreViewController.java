package logic.boundary2;

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
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import logic.beans.SegnalazioneBean;
import logic.controller.RisolviSegnalazioneController;
import logic.observer.Observer;

public class AssegnazioniOperatoreViewController implements Observer {
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
	private Button completaButton;
	@FXML
	private TableView<SegnalazioneBean> tableViewSegnalazioniDaCompletare;
	@FXML
	private TableColumn<SegnalazioneBean, Integer> colIdSegnalazione2;
	@FXML
	private TableColumn<SegnalazioneBean, String> colDescrizione2;
	@FXML
	private TableColumn<SegnalazioneBean, String> colPosizione2;

	private RisolviSegnalazioneController risolviSegnalazioneController = new RisolviSegnalazioneController();
	private static final Logger logger = Logger.getLogger(AssegnazioniOperatoreViewController.class.getName());
	private SegnalazioneBean segnalazioneSelezionata = null;

	@FXML
	public void initialize() {
		AnchorPane sidebar = SidebarLoader.caricaSidebar(SidebarType.OPERATORE_ECOLOGICO_SIDEBAR);
		rootPane.setLeft(sidebar);

		configuraColonne();
		caricaAssegnazioni();
		configuraSelezioneTabella();
		configuraPulsanti();
		aggiungiTooltip();
		attivaDisattivaCompletaButton();

		risolviSegnalazioneController.registraOsservatoreSegnalazioniAssegnate(this);
	}

	private void configuraColonne() {
		colIdSegnalazione.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(cellData.getValue().getIdSegnalazione()).asObject());
		colDescrizione.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescrizione()));
		colPosizione.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosizione()));

		colIdSegnalazione2.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(cellData.getValue().getIdSegnalazione()).asObject());
		colDescrizione2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescrizione()));
		colPosizione2.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosizione()));
	}

	private void caricaAssegnazioni() {
		List<SegnalazioneBean> segnalazioniAssegnate = risolviSegnalazioneController.getSegnalazioniDaRisolvere();
		ObservableList<SegnalazioneBean> segnalazioni = FXCollections.observableArrayList(segnalazioniAssegnate);
		tableViewSegnalazioni.setItems(segnalazioni);
	}

	private void configuraSelezioneTabella() {
		tableViewSegnalazioni.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {
					segnalazioneSelezionata = newValue;
					gestisciSelezioneTabella();
				});

		tableViewSegnalazioni.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2 && tableViewSegnalazioni.getSelectionModel().getSelectedItem() != null) {
				SegnalazioneBean selectedSegnalazione = tableViewSegnalazioni.getSelectionModel().getSelectedItem();
				tableViewSegnalazioni.getItems().remove(selectedSegnalazione);
				tableViewSegnalazioniDaCompletare.getItems().add(selectedSegnalazione);
				attivaDisattivaCompletaButton();
			}
		});

		tableViewSegnalazioniDaCompletare.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2
					&& tableViewSegnalazioniDaCompletare.getSelectionModel().getSelectedItem() != null) {
				SegnalazioneBean selectedSegnalazione = tableViewSegnalazioniDaCompletare.getSelectionModel()
						.getSelectedItem();
				tableViewSegnalazioniDaCompletare.getItems().remove(selectedSegnalazione);
				tableViewSegnalazioni.getItems().add(selectedSegnalazione);
				attivaDisattivaCompletaButton();
			}
		});
	}

	private void gestisciSelezioneTabella() {
		boolean disabled = segnalazioneSelezionata == null;
		fotoButton.setDisable(disabled);
		posizioneButton.setDisable(disabled);
	}

	private void configuraPulsanti() {
		fotoButton.setDisable(true);
		posizioneButton.setDisable(true);
		completaButton.setOnAction(event -> completaSegnalazione());
		fotoButton.setOnAction(event -> DettagliSegnalazione.mostraFoto(segnalazioneSelezionata.getFoto()));
		posizioneButton.setOnAction(event -> DettagliSegnalazione.mostraMappa(segnalazioneSelezionata.getLatitudine(),
				segnalazioneSelezionata.getLongitudine()));
	}

	private void completaSegnalazione() {
		ObservableList<SegnalazioneBean> segnalazioniDaCompletare = tableViewSegnalazioniDaCompletare.getItems();

		if (!segnalazioniDaCompletare.isEmpty()) {
			boolean successoCompletaTutte = true;

			for (SegnalazioneBean segnalazione : segnalazioniDaCompletare) {
				boolean successo = risolviSegnalazioneController.completaSegnalazione(segnalazione);
				if (!successo) {
					successoCompletaTutte = false;
					logger.warning(
							"Errore nel completamento della segnalazione con ID: " + segnalazione.getIdSegnalazione());
				} else {
					logger.info(
							"Segnalazione con ID " + segnalazione.getIdSegnalazione() + " completata con successo.");
				}
			}

			if (successoCompletaTutte) {
				logger.info("Tutte le segnalazioni sono state completate!");
				caricaAssegnazioni();
				tableViewSegnalazioniDaCompletare.getItems().clear();
				completaButton.setDisable(true);
			} else {
				showAlert("Errore Completamento",
						"Si è verificato un errore durante il completamento di alcune segnalazioni.");
			}
		} else {
			showAlert("Nessuna Segnalazione", "Non ci sono segnalazioni da completare.");
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
		List<SegnalazioneBean> segnalazioniAssegnate = risolviSegnalazioneController.getSegnalazioniAssegnate();
		ObservableList<SegnalazioneBean> segnalazioni = FXCollections.observableArrayList(segnalazioniAssegnate);
		tableViewSegnalazioni.setItems(segnalazioni);
	}

	private void aggiungiTooltip() {
		Tooltip tooltipSegnalazioni = new Tooltip("Doppio clic per contrassegnare la segnalazione come completata");
		Tooltip tooltipSegnalazioniDaCompletare = new Tooltip("Doppio clic per rimuovere da questa lista");

		tableViewSegnalazioni.setTooltip(tooltipSegnalazioni);

		tableViewSegnalazioniDaCompletare.setTooltip(tooltipSegnalazioniDaCompletare);
	}

	private void attivaDisattivaCompletaButton() {
		boolean isNotEmpty = !tableViewSegnalazioniDaCompletare.getItems().isEmpty();
		completaButton.setDisable(!isNotEmpty);
	}
}

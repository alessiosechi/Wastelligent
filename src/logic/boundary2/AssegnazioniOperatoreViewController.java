package logic.boundary2;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

	private RisolviSegnalazioneController risolviSegnalazioneController = RisolviSegnalazioneController.getInstance();
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

		risolviSegnalazioneController.registraOsservatoreSegnalazioniAssegnate(this);
	}

	private void configuraColonne() {
		colIdSegnalazione.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdSegnalazione()).asObject());
		colDescrizione.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescrizione()));
		colPosizione.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosizione()));
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
		posizioneButton.setOnAction(event -> DettagliSegnalazione.mostraMappa(segnalazioneSelezionata.getLatitudine(),segnalazioneSelezionata.getLongitudine()));
	}

	private void completaSegnalazione() {
		segnalazioneSelezionata = tableViewSegnalazioni.getSelectionModel().getSelectedItem();

		if (segnalazioneSelezionata != null) {
			boolean successo = risolviSegnalazioneController.completaSegnalazione(segnalazioneSelezionata);
			if (successo) {
				logger.info("Segnalazione completata!");
				caricaAssegnazioni();
			} else {
				showAlert("Errore Completamento",
						"Si è verificato un errore durante il completamento della segnalazione.");
			}
		} else {
			showAlert("Selezione Mancante", "Seleziona una segnalazione.");
		}
	}

	private Optional<ButtonType> showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		return alert.showAndWait();
	}

	@Override
	public void update() {
		List<SegnalazioneBean> segnalazioniAssegnate = risolviSegnalazioneController.getSegnalazioniAssegnate();
		ObservableList<SegnalazioneBean> segnalazioni = FXCollections.observableArrayList(segnalazioniAssegnate);
		tableViewSegnalazioni.setItems(segnalazioni);
	}
}

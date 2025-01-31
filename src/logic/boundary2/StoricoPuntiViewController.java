package logic.boundary2;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import logic.beans.SegnalazioneBean;
import logic.controller.RiscattaRicompensaController;
import logic.boundary.components.ViewInfo;
import logic.boundary.components.ViewLoader;

public class StoricoPuntiViewController {
	@FXML
	private BorderPane rootPane;

	@FXML
	private Button indietroButton;

	@FXML
	private Button fotoButton;
	@FXML
	private Button posizioneButton;
	@FXML
	private TableView<SegnalazioneBean> tableViewSegnalazioni;
	@FXML
	private TableColumn<SegnalazioneBean, String> colDescrizione;
	@FXML
	private TableColumn<SegnalazioneBean, String> colPosizione;
	@FXML
	private TableColumn<SegnalazioneBean, Integer> colPunti;

	private RiscattaRicompensaController riscattaRicompensaController = RiscattaRicompensaController.getInstance();
	private SegnalazioneBean segnalazioneSelezionata = null;

	@FXML
	public void initialize() {
		AnchorPane sidebar = SidebarLoader.caricaSidebar(SidebarType.UTENTE_BASE_SIDEBAR);
		rootPane.setLeft(sidebar);

		configuraColonneTabella();

		caricaSegnalazioni();

		configuraPulsanti();

		tableViewSegnalazioni.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> {
					segnalazioneSelezionata = newValue;

					// Abilita i pulsanti solo se una segnalazione è selezionata
					boolean selezioneValida = (newValue != null);
					fotoButton.setDisable(!selezioneValida);
					posizioneButton.setDisable(!selezioneValida);
				});
	}

	private void caricaSegnalazioni() {
		ObservableList<SegnalazioneBean> segnalazioni = FXCollections
				.observableArrayList(riscattaRicompensaController.ottieniSegnalazioniUtente());

		tableViewSegnalazioni.setItems(segnalazioni);
	}

	private void configuraColonneTabella() {

		colDescrizione.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescrizione()));
		colPosizione.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosizione()));
		colPunti.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(cellData.getValue().getPuntiAssegnati()).asObject());
	}

	private void configuraPulsanti() {
		fotoButton.setDisable(true);
		posizioneButton.setDisable(true);
		indietroButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.EFFETTUA_RISCATTO_VIEW));

		fotoButton.setOnAction(event -> {
			if (segnalazioneSelezionata != null) {
				DettagliSegnalazione.mostraFoto(segnalazioneSelezionata.getFoto());
			} else {
				showAlert(Alert.AlertType.WARNING, "Avviso", "Seleziona prima una segnalazione.");
			}
		});

		posizioneButton.setOnAction(event -> {
			if (segnalazioneSelezionata != null) {
				DettagliSegnalazione.mostraMappa(segnalazioneSelezionata.getLatitudine(),
						segnalazioneSelezionata.getLongitudine());
			} else {
				showAlert(Alert.AlertType.WARNING, "Avviso", "Seleziona prima una segnalazione.");
			}
		});
	}

	private void showAlert(Alert.AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.showAndWait();
	}

}

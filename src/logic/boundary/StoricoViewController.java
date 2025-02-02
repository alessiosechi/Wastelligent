
package logic.boundary;

import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import logic.beans.SegnalazioneBean;
import logic.boundary.components.ViewInfo;
import logic.boundary.components.ViewLoader;
import logic.controller.RiscattaRicompensaController;

public class StoricoViewController {

	@FXML
	private Button indietroButton;
	@FXML
	private Button exitButton;
	@FXML
	private Button riscattaRicompensaButton;
	@FXML
	private Button nuovaSegnalazioneButton;
	@FXML
	private Button vediDettagliButton;

	@FXML
	private TableView<SegnalazioneBean> tableViewSegnalazioni;
	@FXML
	private TableColumn<SegnalazioneBean, String> colDescrizione;
	@FXML
	private TableColumn<SegnalazioneBean, String> colPosizione;
	@FXML
	private TableColumn<SegnalazioneBean, Integer> colPunti;

	private RiscattaRicompensaController riscattaRicompensaController = RiscattaRicompensaController.getInstance();

	@FXML
	public void initialize() {
		configuraColonneTabella();
		caricaSegnalazioni();
		configuraPulsanti();
		impostaListenerSelezione();
	}

	private void caricaSegnalazioni() {
		List<SegnalazioneBean> segnalazioni = riscattaRicompensaController.ottieniSegnalazioniUtente();

		ObservableList<SegnalazioneBean> segnalazioniRiscontrate = FXCollections.observableArrayList(segnalazioni);
		tableViewSegnalazioni.setItems(segnalazioniRiscontrate);
	}

	private void configuraColonneTabella() {
		colDescrizione.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescrizione()));
		colPosizione.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosizione()));
		colPunti.setCellValueFactory(
				cellData -> new SimpleIntegerProperty(cellData.getValue().getPuntiAssegnati()).asObject());
	}

	private void configuraPulsanti() {
		vediDettagliButton.setDisable(true);

		vediDettagliButton.setOnAction(event -> ViewLoader.caricaDettagliSegnalazioneView(
				tableViewSegnalazioni.getSelectionModel().getSelectedItem(), CallerType.CONTROLLER4));
		indietroButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.RISCATTA_RICOMPENSA_VIEW));
		exitButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.LOGIN_VIEW));
		riscattaRicompensaButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.RISCATTA_RICOMPENSA_VIEW));
		nuovaSegnalazioneButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.EFFETTUA_SEGNALAZIONE_VIEW));
	}

	private void impostaListenerSelezione() {
		tableViewSegnalazioni.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> 
				    vediDettagliButton.setDisable(newValue == null)
				);
	}

}

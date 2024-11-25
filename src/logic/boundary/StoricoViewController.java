
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
import logic.controller.RiscattaRicompensaController;
import logic.model.domain.SegnalazioneBean;

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
    
   
    
	private DettagliSegnalazioneViewController dettagliSegnalazioneViewController = DettagliSegnalazioneViewController.getInstance();
	private RiscattaRicompensaController riscattaRicompensaController = RiscattaRicompensaController.getInstance();
    private static StoricoViewController instance;
    
    
    
    @FXML
    public void initialize() {	
        configuraColonneTabella();  
        caricaSegnalazioni();
        configuraPulsanti();
        impostaListenerSelezione();
    }
    
    private void caricaSegnalazioni() {
		
        List<SegnalazioneBean> segnalazioni = riscattaRicompensaController.ottieniSegnalazioniRiscontrate();

        ObservableList<SegnalazioneBean> segnalazioniRiscontrate = FXCollections.observableArrayList(segnalazioni);
        tableViewSegnalazioni.setItems(segnalazioniRiscontrate);
    }

    private void configuraColonneTabella() {
		colDescrizione.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescrizione()));
		colPosizione.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosizione()));
		colPunti.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPuntiAssegnati()).asObject());
    }
    
	private void configuraPulsanti() {
		vediDettagliButton.setDisable(true);
		
		vediDettagliButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.DETTAGLI_VIEW));
		indietroButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.RISCATTA_RICOMPENSA_VIEW));
		exitButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.LOGIN_VIEW));
		riscattaRicompensaButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.RISCATTA_RICOMPENSA_VIEW));
		nuovaSegnalazioneButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.EFFETTUA_SEGNALAZIONE_VIEW));
	}
	
	private void impostaListenerSelezione() {
        tableViewSegnalazioni.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                vediDettagliButton.setDisable(false);
                                      
                dettagliSegnalazioneViewController.setSegnalazioneBean(newValue);
                dettagliSegnalazioneViewController.setCallerType(CallerType.CONTROLLER4);

            } else {
                vediDettagliButton.setDisable(true);
            }
        });
	}


    public static StoricoViewController getInstance() {
        if (instance == null) {
            instance = new StoricoViewController();
        }
        return instance;
    }
    


}


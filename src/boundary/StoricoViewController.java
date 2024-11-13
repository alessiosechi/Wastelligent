
package boundary;

import java.util.List;

import controller.RiscattaRicompensaController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import model.domain.SegnalazioneBean;
import javafx.scene.control.TableView;

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
    private Stage primaryStage;
    
    
    
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
		
		vediDettagliButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.DETTAGLI_VIEW, primaryStage));
		indietroButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.RISCATTA_RICOMPENSA_VIEW, primaryStage));
		exitButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.LOGIN_VIEW, primaryStage));
		riscattaRicompensaButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.RISCATTA_RICOMPENSA_VIEW, primaryStage));
		nuovaSegnalazioneButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.EFFETTUA_SEGNALAZIONE_VIEW, primaryStage));
	}
	
	private void impostaListenerSelezione() {
        tableViewSegnalazioni.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // abilito il pulsante "DETTAGLI" quando una segnalazione è selezionata
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
    
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


}


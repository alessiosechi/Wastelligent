
package boundary;

import java.util.List;

import controller.LoginController;
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
import model.domain.UtenteBean;
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
	private LoginController loginController = LoginController.getInstance();
    private static StoricoViewController instance;
    private Stage primaryStage;



    @FXML
    public void initialize() {
    	
		colDescrizione.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescrizione()));
		colPosizione.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosizione()));
		colPunti.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPuntiAssegnati()).asObject());
        
        caricaSegnalazioni();
    	
        vediDettagliButton.setDisable(true);

        tableViewSegnalazioni.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                // abilito il pulsante "DETTAGLI" quando una segnalazione è selezionata
                vediDettagliButton.setDisable(false);
                           
                dettagliSegnalazioneViewController.setSegnalazioneBean(newValue);
                dettagliSegnalazioneViewController.setState(new StoricoViewState());

            } else {
                vediDettagliButton.setDisable(true);
            }
        });
        
        vediDettagliButton.setOnAction(event -> {
            ViewLoader.caricaView("DettagliSegnalazioneView.fxml", primaryStage);
        });  
        

        indietroButton.setOnAction(event -> { 	
        	ViewLoader.caricaView("RiscattaRicompensaView.fxml", primaryStage);     		
        });
        
        exitButton.setOnAction(event -> {	
        	ViewLoader.caricaView("LoginView.fxml", primaryStage);   	     	
        });
    	
        riscattaRicompensaButton.setOnAction(event -> {   	
        	ViewLoader.caricaView("RiscattaRicompensaView.fxml", primaryStage); 	
        });
           
        nuovaSegnalazioneButton.setOnAction(event -> {	
        	ViewLoader.caricaView("EffettuaSegnalazioneView.fxml", primaryStage);
        		
        });
    }

    
    
    private void caricaSegnalazioni() {
		UtenteBean utente = loginController.getUtente();
		int idUtente = utente.getIdUtente();
		
        List<SegnalazioneBean> segnalazioni = riscattaRicompensaController.ottieniSegnalazioniRiscontrate(idUtente);

        ObservableList<SegnalazioneBean> segnalazioniRiscontrate = FXCollections.observableArrayList(segnalazioni);
        tableViewSegnalazioni.setItems(segnalazioniRiscontrate);
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


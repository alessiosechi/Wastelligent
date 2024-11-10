package boundary;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import controller.RisolviSegnalazioneController;
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
import javafx.stage.Stage;
import model.domain.SegnalazioneBean;

public class SegnalazioniAssegnateViewController {
	
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
	private Stage primaryStage;
    private static SegnalazioniAssegnateViewController instance;
	private DettagliSegnalazioneViewController dettagliSegnalazioneViewController = DettagliSegnalazioneViewController.getInstance();
	private static final Logger logger = Logger.getLogger(SegnalazioniAssegnateViewController.class.getName());
    
    
    public void initialize() {
		exitButton.setOnAction(event -> ViewLoader.caricaView("LoginView.fxml", primaryStage));
		
		caricaAssegnazioni();

        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdSegnalazione()).asObject());
        descrizioneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescrizione()));
        posizioneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosizione()));

        dettagliButton.setDisable(true);

        segnalazioniTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {

                dettagliButton.setDisable(false);


                dettagliSegnalazioneViewController.setSegnalazioneBean(newValue);
                dettagliSegnalazioneViewController.setState(new SegnalazioniAssegnateViewState());
            } else {
                dettagliButton.setDisable(true);
            }
        });
        
        dettagliButton.setOnAction(event -> 
            ViewLoader.caricaView("DettagliSegnalazioneView.fxml", primaryStage)
        ); 
        

        completaButton.setOnAction(event -> {

            SegnalazioneBean segnalazioneSelezionata = segnalazioniTable.getSelectionModel().getSelectedItem();

            if (segnalazioneSelezionata != null) {

            	int idSegnalazione= segnalazioneSelezionata.getIdSegnalazione();
                boolean successo = risolviSegnalazioneController.completaSegnalazione(idSegnalazione);

                if (successo) {
                    logger.info("Segnalazione completata!");

                	ViewLoader.caricaView("SegnalazioniAssegnateView.fxml", primaryStage);
                } else {
                    showAlert("Errore Completamento", "Si è verificato un errore durante il completamento della segnalazione.");
                }
            } else {

                showAlert("Selezione Mancante", "Seleziona una segnalazione.");
            }
        });
         
    }
        

    private Optional<ButtonType> showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        return alert.showAndWait();
    }
        

    private void caricaAssegnazioni() {
    	
        List<SegnalazioneBean> segnalazioniAssegnate = risolviSegnalazioneController.getSegnalazioniAssegnate();
          
        ObservableList<SegnalazioneBean> segnalazioni = FXCollections.observableArrayList(segnalazioniAssegnate);
        segnalazioniTable.setItems(segnalazioni);
    }
        
        
        
    public void setPrimaryStage(Stage primaryStage)
    {
    	this.primaryStage= primaryStage;
    }
    
    
    public static SegnalazioniAssegnateViewController getInstance() {
        if (instance == null) {
            instance = new SegnalazioniAssegnateViewController();
        }
        return instance;
    }
	
}

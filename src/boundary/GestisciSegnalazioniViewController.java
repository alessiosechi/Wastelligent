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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import logic.observer.Observer;
import model.domain.AssegnazioneBean;
import model.domain.SegnalazioneBean;
import model.domain.OperatoreEcologicoBean;

public class GestisciSegnalazioniViewController implements Observer{

    @FXML
    private Button exitButton;
    @FXML
    private Button assegnaButton;
    @FXML
    private Button assegnaPuntiButton;
    @FXML
    private Button vediDettagliButton;
    @FXML
    private Button eliminaButton;
    
    @FXML
    private TableView<SegnalazioneBean> segnalazioniTable; 
    @FXML
    private TableColumn<SegnalazioneBean, Integer> idColumn;
    @FXML
    private TableColumn<SegnalazioneBean, String> descrizioneColumn;
    @FXML
    private TableColumn<SegnalazioneBean, String> posizioneColumn;
    @FXML
    private TableColumn<SegnalazioneBean, String> statoColumn;
    
    @FXML
    private ComboBox<OperatoreEcologicoBean> operatoriEcologiciComboBox;

	
    private static GestisciSegnalazioniViewController instance;
	private DettagliSegnalazioneViewController dettagliSegnalazioneViewController = DettagliSegnalazioneViewController.getInstance();
    private RisolviSegnalazioneController risolviSegnalazioneController = RisolviSegnalazioneController.getInstance();
    private Stage primaryStage;
	private static final Logger logger = Logger.getLogger(GestisciSegnalazioniViewController.class.getName());
    private boolean osservatoreRegistrato = false; 
    
	private void configureButtons() {
	    exitButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.LOGIN_VIEW, primaryStage));
	    vediDettagliButton.setDisable(true);
	    eliminaButton.setDisable(true);
	}
	private void configureColumns() {
	    idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdSegnalazione()).asObject());
	    descrizioneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescrizione()));
	    posizioneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosizione()));
	    statoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStato()));
	}
	private void configureTableSelection() {
	    segnalazioniTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> 
	        handleTableSelection(newValue)
	    );
	}
	private void handleTableSelection(SegnalazioneBean newValue) {
	    if (newValue != null) {
	        vediDettagliButton.setDisable(false);
	        eliminaButton.setDisable(false);
	        
            dettagliSegnalazioneViewController.setSegnalazioneBean(newValue);
            dettagliSegnalazioneViewController.setCallerType(CallerType.CONTROLLER2);
	    } else {
	        vediDettagliButton.setDisable(true);
	        eliminaButton.setDisable(true);
	        operatoriEcologiciComboBox.getSelectionModel().clearSelection();
	    }
	}
	private void configureHandlers() {
	    vediDettagliButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.DETTAGLI_VIEW, primaryStage));
	    eliminaButton.setOnAction(event -> handleEliminaAction());
	    assegnaButton.setOnAction(event -> handleAssegnaAction());
	    assegnaPuntiButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.ASSEGNA_PUNTI_VIEW, primaryStage));
	}
	private void handleEliminaAction() {
	    Optional<ButtonType> result = showAlert("Conferma Eliminazione", "Sei sicuro di voler eliminare la segnalazione selezionata?");
	    if (result.isPresent() && result.get() == ButtonType.OK) {
	        SegnalazioneBean segnalazioneSelezionata = segnalazioniTable.getSelectionModel().getSelectedItem();
	        if (segnalazioneSelezionata != null) {
	            risolviSegnalazioneController.eliminaSegnalazione(segnalazioneSelezionata);
	        } else {
	            logger.info("Nessuna segnalazione selezionata.");
	        }
	    } else {
	        logger.info("Eliminazione annullata.");
	    }
	}

	private void handleAssegnaAction() {
	    OperatoreEcologicoBean operatoreSelezionato = operatoriEcologiciComboBox.getSelectionModel().getSelectedItem();
	    SegnalazioneBean segnalazioneSelezionata = segnalazioniTable.getSelectionModel().getSelectedItem();
	    if (operatoreSelezionato != null && segnalazioneSelezionata != null) {

	        AssegnazioneBean assegnazioneBean = new AssegnazioneBean();
	        assegnazioneBean.setSegnalazione(segnalazioneSelezionata);
	        assegnazioneBean.setOperatore(operatoreSelezionato);
	        
	        if (risolviSegnalazioneController.assegnaOperatore(assegnazioneBean)) {
	            logger.info("Segnalazione assegnata con successo a " + operatoreSelezionato.getUsername());

	        } else {
	            showAlert("Errore Assegnazione", "Si è verificato un errore durante l'assegnazione della segnalazione.");
	        }
	    } else {
	        showAlert("Selezione Mancante", "Seleziona sia un operatore che una segnalazione per procedere con l'assegnazione.");
	    }
	}
    
    public void initialize() {
        configureButtons();
        configureColumns();
		caricaSegnalazioniDaRisolvere();
	    caricaOperatoriEcologici();
        configureTableSelection();
        configureHandlers();
        
        // Verifica se l'osservatore è già stato registrato
        if (!osservatoreRegistrato) {
            risolviSegnalazioneController.registraOsservatoreSegnalazioniAttive(this);
            osservatoreRegistrato = true;  // Segna l'osservatore come registrato
        }
    }

    private void caricaSegnalazioniDaRisolvere() {
        List<SegnalazioneBean> segnalazioniDaRisolvere = risolviSegnalazioneController.getSegnalazioniRicevute();
  
        ObservableList<SegnalazioneBean> segnalazioniData = FXCollections.observableArrayList(segnalazioniDaRisolvere);
        segnalazioniTable.setItems(segnalazioniData);
    }
    

    private void caricaOperatoriEcologici() {
    	
        // ottengo la lista di operatori dal controller applicativo
        List<OperatoreEcologicoBean> operatoriEcologici = risolviSegnalazioneController.getOperatoriEcologiciDisponibili();

        ObservableList<OperatoreEcologicoBean> operatori = FXCollections.observableArrayList(operatoriEcologici);
        operatoriEcologiciComboBox.setItems(operatori);
        

        operatoriEcologiciComboBox.setCellFactory(cell -> new ListCell<OperatoreEcologicoBean>() {
            @Override
            protected void updateItem(OperatoreEcologicoBean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getUsername());
            }
        });
        

        operatoriEcologiciComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(OperatoreEcologicoBean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Seleziona Operatore" : item.getUsername());
            }
        });

    }


    public static GestisciSegnalazioniViewController getInstance() {
        if (instance == null) {
            instance = new GestisciSegnalazioniViewController();
        }
        return instance;
    }
    
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
    
    
    private Optional<ButtonType> showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        return alert.showAndWait();
    }
    
    
    
    
	@Override
	public void update() {
        List<SegnalazioneBean> segnalazioniDaRisolvere = risolviSegnalazioneController.getSegnalazioniAttive();   
        ObservableList<SegnalazioneBean> segnalazioniData = FXCollections.observableArrayList(segnalazioniDaRisolvere);
        segnalazioniTable.setItems(segnalazioniData);
		
	}

}

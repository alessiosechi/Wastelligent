package logic.boundary;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import logic.beans.OperatoreEcologicoBean;
import logic.beans.SegnalazioneBean;
import logic.controller.RisolviSegnalazioneController;
import logic.exceptions.OperatoreNonDisponibileException;
import logic.observer.Observer;

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
    private ComboBox<String> operatoriEcologiciComboBox;
	
    private static GestisciSegnalazioniViewController instance;
	private DettagliSegnalazioneViewController dettagliSegnalazioneViewController = DettagliSegnalazioneViewController.getInstance();
    private RisolviSegnalazioneController risolviSegnalazioneController = RisolviSegnalazioneController.getInstance();
	private static final Logger logger = Logger.getLogger(GestisciSegnalazioniViewController.class.getName());
    private boolean osservatoreRegistrato = false; 
    private List<OperatoreEcologicoBean> operatoriEcologici;
    
	private void configureButtons() {
	    exitButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.LOGIN_VIEW));
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
	    vediDettagliButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.DETTAGLI_VIEW));
	    eliminaButton.setOnAction(event -> handleEliminaAction());
	    assegnaButton.setOnAction(event -> handleAssegnaAction());
	    assegnaPuntiButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.ASSEGNA_PUNTI_VIEW));
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
	    int selectedIndex = operatoriEcologiciComboBox.getSelectionModel().getSelectedIndex();
	    OperatoreEcologicoBean operatoreSelezionato = operatoriEcologici.get(selectedIndex);

	    SegnalazioneBean segnalazioneSelezionata = segnalazioniTable.getSelectionModel().getSelectedItem();
	    
	    if (operatoreSelezionato != null && segnalazioneSelezionata != null) {
	        try {
	            if (risolviSegnalazioneController.assegnaOperatore(segnalazioneSelezionata, operatoreSelezionato)) {
	                logger.info("Segnalazione assegnata con successo a " + operatoreSelezionato.getUsername());
	            } else {
	                showAlert("Errore Assegnazione", "Si è verificato un errore durante l'assegnazione della segnalazione.");
	            }
	        } catch (OperatoreNonDisponibileException e) {
	            showAlert("Operatore Non Disponibile", e.getMessage());
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
        
        // verifico se l'osservatore è già stato registrato
        if (!osservatoreRegistrato) {
            risolviSegnalazioneController.registraOsservatoreSegnalazioniAttive(this);
            osservatoreRegistrato = true;  
        }
    }

    private void caricaSegnalazioniDaRisolvere() {
    	
        List<SegnalazioneBean> segnalazioniDaRisolvere = risolviSegnalazioneController.getSegnalazioniRicevute();
  
        ObservableList<SegnalazioneBean> segnalazioniData = FXCollections.observableArrayList(segnalazioniDaRisolvere);
        segnalazioniTable.setItems(segnalazioniData);
    }
    

    private void caricaOperatoriEcologici() {
    	
        // ottengo la lista di operatori dal controller applicativo
    	operatoriEcologici = risolviSegnalazioneController.getOperatoriEcologiciDisponibili();

        ObservableList<String> operatori = FXCollections.observableArrayList();
        operatoriEcologici.forEach(o ->operatori.add(o.getUsername()));
        operatoriEcologiciComboBox.setItems(operatori);
    }

    public static GestisciSegnalazioniViewController getInstance() {
        if (instance == null) {
            instance = new GestisciSegnalazioniViewController();
        }
        return instance;
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

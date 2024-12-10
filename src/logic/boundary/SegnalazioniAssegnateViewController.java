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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import logic.beans.SegnalazioneBean;
import logic.controller.RisolviSegnalazioneController;
import logic.observer.Observer;

public class SegnalazioniAssegnateViewController implements Observer{
	
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
	private DettagliSegnalazioneViewController dettagliSegnalazioneViewController = DettagliSegnalazioneViewController.getInstance();
	private static final Logger logger = Logger.getLogger(SegnalazioniAssegnateViewController.class.getName());
    private boolean osservatoreRegistrato = false; 
    
    public void initialize() {
		exitButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.LOGIN_VIEW));
		
		caricaAssegnazioni();

        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdSegnalazione()).asObject());
        descrizioneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescrizione()));
        posizioneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosizione()));

        dettagliButton.setDisable(true);

        segnalazioniTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                dettagliButton.setDisable(false);

                dettagliSegnalazioneViewController.setSegnalazioneBean(newValue);
                dettagliSegnalazioneViewController.setCallerType(CallerType.CONTROLLER3);
            } else {
                dettagliButton.setDisable(true);
            }
        });
        
        dettagliButton.setOnAction(event -> 
            ViewLoader.caricaView(ViewInfo.DETTAGLI_VIEW)
        );     

        completaButton.setOnAction(event -> {

            SegnalazioneBean segnalazioneSelezionata = segnalazioniTable.getSelectionModel().getSelectedItem();

            if (segnalazioneSelezionata != null) {

                boolean successo = risolviSegnalazioneController.completaSegnalazione(segnalazioneSelezionata);

                if (successo) {
                    logger.info("Segnalazione completata!");

                } else {
                    showAlert("Errore Completamento", "Si è verificato un errore durante il completamento della segnalazione.");
                }
            } else {

                showAlert("Selezione Mancante", "Seleziona una segnalazione.");
            }
        });
        
        if (!osservatoreRegistrato) {
        	risolviSegnalazioneController.registraOsservatoreSegnalazioniAssegnate(this);
            osservatoreRegistrato = true; 
        }
    }     

    private Optional<ButtonType> showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        return alert.showAndWait();
    }
        
    private void caricaAssegnazioni() {
    	
        List<SegnalazioneBean> segnalazioniAssegnate = risolviSegnalazioneController.getSegnalazioniDaRisolvere();
          
        ObservableList<SegnalazioneBean> segnalazioni = FXCollections.observableArrayList(segnalazioniAssegnate);
        segnalazioniTable.setItems(segnalazioni);
    }

	@Override
	public void update() {
        List<SegnalazioneBean> segnalazioniAssegnate = risolviSegnalazioneController.getSegnalazioniAssegnate();
        
        ObservableList<SegnalazioneBean> segnalazioni = FXCollections.observableArrayList(segnalazioniAssegnate);
        segnalazioniTable.setItems(segnalazioni);	
	}
	
}

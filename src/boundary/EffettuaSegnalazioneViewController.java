package boundary;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Configuration;
import com.sothawo.mapjfx.Projection;
import com.sothawo.mapjfx.event.MapViewEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import exceptions.SegnalazioneVicinaException;
import controller.EffettuaSegnalazioneController;
import model.domain.PosizioneBean;
import model.domain.SegnalazioneBean;



import java.io.File;

public class EffettuaSegnalazioneViewController {
	
	@FXML
	private Button redeemRewardButton; 
	@FXML
	private Button exitButton;
    @FXML
    private TextArea descriptionField;  
    @FXML
    private TextArea photoField; 
    @FXML
    private Button browseButton;  
    @FXML
    private Button submitButton;  
    @FXML
    private TextArea searchField; 
    @FXML
    private Button searchButton; 
    @FXML
    private Button resetButton;  
    @FXML
    private Pane mapPane; 

    private MapView mapView; 
    private Marker currentMarker;
    
    private static EffettuaSegnalazioneViewController instance;
    private EffettuaSegnalazioneController effettuaSegnalazioneController = EffettuaSegnalazioneController.getInstance();
    private Stage primaryStage;


    @FXML
    public void initialize() { // questo metodo viene chiamato automaticamente per l'inizializzazione dell'interfaccia
    	
        // inizializzazione della mappa
        mapView = new MapView();
        mapView.initialize(Configuration.builder()
                .projection(Projection.WEB_MERCATOR)
                .showZoomControls(true)
                .build());

        mapView.setZoom(12); // imposto lo zoom a 12
        mapView.setCenter(new Coordinate(41.9028, 12.4964)); // coordinate di Roma

        // aggiungo la mappa al Pane
        mapPane.getChildren().add(mapView);
        mapView.setPrefWidth(mapPane.getPrefWidth());
        mapView.setPrefHeight(mapPane.getPrefHeight());

        // se clicco sulla mappa viene creato un marker sulla posizione cliccata
        mapView.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
            Coordinate coordinate = event.getCoordinate().normalize();
            placeMarker(coordinate);
        });

        // azione associata al pulsante browseButton
        browseButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                photoField.setText(selectedFile.getAbsolutePath());
            }
        });

        // azione associata al pulsante submitButton
        submitButton.setOnAction(event -> {
            String description = descriptionField.getText();
            String photoPath = photoField.getText();
            boolean success= inviaSegnalazione(description, photoPath);
            if(success) {
                ViewLoader.caricaView("EffettuaSegnalazioneView.fxml", primaryStage);
            }
        });

        // azione associata al pulsante searchButton
        searchButton.setOnAction(event -> {
            String query = searchField.getText();
            if (!query.isEmpty()) {
                try {
                	// ottengo le coordinate della posizione cercata
                    PosizioneBean posizioneBean = effettuaSegnalazioneController.getCoordinates(query);
                    Coordinate coordinate = new Coordinate(posizioneBean.getLatitudine(), posizioneBean.getLongitudine());

                    mapView.setCenter(coordinate);
                    placeMarker(coordinate);
                } catch (Exception e) {
                    showAlert("Errore", "Impossibile trovare la posizione.");
                }
            }
        });

        // azione associata al pulsante resetButton
        resetButton.setOnAction(event -> {
            mapView.setCenter(new Coordinate(41.9028, 12.4964)); // reset sulla posizione di Roma

            searchField.clear();
            descriptionField.clear();
            photoField.clear();
        });
        
        
        // azione associata al pulsante redeemRewardButton
        redeemRewardButton.setOnAction(event -> 	
        	ViewLoader.caricaView("RiscattaRicompensaView.fxml", primaryStage)
        );
        
        // azione associata al pulsante exitButton
        exitButton.setOnAction(event ->
        	ViewLoader.caricaView("LoginView.fxml", primaryStage)	
        );
             
    }

    private void placeMarker(Coordinate coordinate) {
        if (currentMarker != null) {
            mapView.removeMarker(currentMarker);
        }

        currentMarker = Marker.createProvided(Marker.Provided.RED).setPosition(coordinate).setVisible(true);
        mapView.addMarker(currentMarker);
    }


    private boolean inviaSegnalazione(String description, String photoPath) {
        try {
        	
            SegnalazioneBean segnalazioneBean = new SegnalazioneBean();
            segnalazioneBean.setDescrizione(description);
            segnalazioneBean.setFoto(photoPath);

            // uso la posizione corrente del marker
            if (currentMarker != null) {
                segnalazioneBean.setLatitudine(currentMarker.getPosition().getLatitude());
                segnalazioneBean.setLongitudine(currentMarker.getPosition().getLongitude());
            }

            // invio della segnalazione
            effettuaSegnalazioneController.inviaSegnalazione(segnalazioneBean);

            // messaggio di successo
            showAlert("Successo", "Segnalazione inviata con successo!");
            return true;

        } catch (SegnalazioneVicinaException e) {
            showAlert("Attenzione", "Esiste già una segnalazione nelle vicinanze!");
            return false;

        } catch (Exception e) {
            showAlert("Errore", "Si è verificato un errore durante l'invio della segnalazione.");
            return false;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // singleton: retituisco l'istanza di EffettuaSegnalazioneViewController
    public static EffettuaSegnalazioneViewController getInstance() {
        if (instance == null) {
            instance = new EffettuaSegnalazioneViewController();
        }
        return instance;
    }
    
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    
    
}

package boundary;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Configuration;
import com.sothawo.mapjfx.Projection;
import com.sothawo.mapjfx.event.MapViewEvent;

import controller.LoginController;
import exceptions.SegnalazioneVicinaException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import model.domain.PosizioneBean;
import model.domain.SegnalazioneBean;
import model.domain.UtenteBean;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import controller.EffettuaSegnalazioneController;

public class EffettuaSegnalazioneViewController1 {

    @FXML
    private MapView mapView;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField photoField;
    @FXML
    private Button browseButton;
    @FXML
    private Button submitButton;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private Button resetButton;
    @FXML
    private Label coordinatesLabel;


    private Marker currentMarker;
    private List<Marker> markers = new ArrayList<>();
	private EffettuaSegnalazioneController effettuaSegnalazioneController = EffettuaSegnalazioneController.getInstance();
	private LoginController loginController = LoginController.getInstance();

    @FXML
    public void initialize() {
        // Configurazione mappa
        mapView.initialize(Configuration.builder()
                .projection(Projection.WEB_MERCATOR)
                .showZoomControls(true)
                .build());

        mapView.setZoom(12);
        mapView.setCenter(new Coordinate(41.9028, 12.4964)); // Coordinate di Roma, Italia

        mapView.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
            Coordinate coordinate = event.getCoordinate().normalize();
            placeMarker(coordinate);
        });

        browseButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                photoField.setText(selectedFile.getAbsolutePath());
            }
        });

        submitButton.setOnAction(event -> {
            String description = descriptionField.getText();
            String photoPath = photoField.getText();
            inviaSegnalazione(description, photoPath);
        });

        searchButton.setOnAction(event -> {
            String query = searchField.getText();
            if (!query.isEmpty()) {
                try {
                    PosizioneBean posizioneBean = effettuaSegnalazioneController.getCoordinates(query);
                    Coordinate coordinate = new Coordinate(posizioneBean.getLatitudine(), posizioneBean.getLongitudine());
                    
                    
                    /*
                     *  Deve essere il controller grafico a "costruire" l'oggetto Coordinate, infatti, non sarebbe coorretto se 
                     *  il controller applicativo restituisse un oggetto Coordinate perchè in quel modo ci sarebbe la dipendenza 
                     *  dall'API utilizzata.
                     */
                    
                    mapView.setCenter(coordinate);
                    placeMarker(coordinate);
                    coordinatesLabel.setText(coordinate.getLatitude() + ", " + coordinate.getLongitude());
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Errore", "Impossibile trovare la posizione.");
                }
            }
        });

        resetButton.setOnAction(event -> {
            mapView.setCenter(new Coordinate(41.9028, 12.4964));
            removeAllMarkers();
            searchField.clear();
            coordinatesLabel.setText("Posizione non selezionata");
            currentMarker = null;
        });
    }


    private void placeMarker(Coordinate coordinate) {
        if (currentMarker != null) {
            mapView.removeMarker(currentMarker);
            markers.remove(currentMarker);
        }

        currentMarker = Marker.createProvided(Marker.Provided.RED).setPosition(coordinate).setVisible(true);
        mapView.addMarker(currentMarker);
        markers.add(currentMarker);
        coordinatesLabel.setText(coordinate.getLatitude() + ", " + coordinate.getLongitude());
    }

    private void removeAllMarkers() {
        for (Marker marker : markers) {
            mapView.removeMarker(marker);
        }
        markers.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void inviaSegnalazione(String description, String photoPath) {
        try {
            // Creazione dell'oggetto SegnalazioneBean
            SegnalazioneBean segnalazioneBean = new SegnalazioneBean();
            segnalazioneBean.setDescrizione(description);
            segnalazioneBean.setFoto(photoPath);
            segnalazioneBean.setLatitudine(currentMarker.getPosition().getLatitude());
            segnalazioneBean.setLongitudine(currentMarker.getPosition().getLongitude());

            UtenteBean utenteBean = loginController.getUtente();

            segnalazioneBean.setIdUtente(utenteBean.getIdUtente());
            
            
            // Passaggio del SegnalazioneBean al controller applicativo
            effettuaSegnalazioneController.inviaSegnalazione(segnalazioneBean);


            showAlert("Successo", "Segnalazione inviata con successo!");
        }catch (SegnalazioneVicinaException e) {
            // Gestisce l’eccezione specifica per la segnalazione vicina
            showAlert("Attenzione", "Esiste già una segnalazione nelle vicinanze!");

        }  catch (Exception e) {
            e.printStackTrace();
            showAlert("Errore", "Si è verificato un errore durante l'invio della segnalazione.");
        }
    }

}

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

import controller.LoginController;
import exceptions.SegnalazioneVicinaException;
import controller.EffettuaSegnalazioneController;
import model.domain.PosizioneBean;
import model.domain.SegnalazioneBean;
import model.domain.UtenteBean;

import java.io.File;

public class EffettuaSegnalazioneViewController2 {

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
    private LoginController loginController = LoginController.getInstance();
    private Stage primaryStage;

    @FXML
    public void initialize() {
        // Initialize map
        mapView = new MapView();
        mapView.initialize(Configuration.builder()
                .projection(Projection.WEB_MERCATOR)
                .showZoomControls(true)
                .build());

        mapView.setZoom(12);
        mapView.setCenter(new Coordinate(41.9028, 12.4964)); // Rome coordinates
        mapPane.getChildren().add(mapView);
        mapView.setPrefWidth(mapPane.getPrefWidth());
        mapView.setPrefHeight(mapPane.getPrefHeight());

        // Handle map click
        mapView.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
            Coordinate coordinate = event.getCoordinate().normalize();
            placeMarker(coordinate);
        });

        // Browse button action
        browseButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                photoField.setText(selectedFile.getAbsolutePath());
            }
        });

        // Submit action
        submitButton.setOnAction(event -> {
            String description = descriptionField.getText();
            String photoPath = photoField.getText();
            inviaSegnalazione(description, photoPath);
        });

        // Search action
        searchButton.setOnAction(event -> {
            String query = searchField.getText();
            if (!query.isEmpty()) {
                try {
                    PosizioneBean posizioneBean = effettuaSegnalazioneController.getCoordinates(query);
                    Coordinate coordinate = new Coordinate(posizioneBean.getLatitudine(), posizioneBean.getLongitudine());
                    mapView.setCenter(coordinate);
                    placeMarker(coordinate);
                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert("Errore", "Impossibile trovare la posizione.");
                }
            }
        });

        // Reset action
        resetButton.setOnAction(event -> {
            mapView.setCenter(new Coordinate(41.9028, 12.4964)); // Reset to Rome
            searchField.clear();
            descriptionField.clear();
            photoField.clear();
        });

        // Reward button action
        redeemRewardButton.setOnAction(event -> 
            ViewLoader.caricaView("RiscattaRicompensaView.fxml", primaryStage)
        );

        // Exit button action
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

    private void inviaSegnalazione(String description, String photoPath) {
        try {
            SegnalazioneBean segnalazioneBean = new SegnalazioneBean();
            segnalazioneBean.setDescrizione(description);
            segnalazioneBean.setFoto(photoPath);

            // Use current marker position
            if (currentMarker != null) {
                segnalazioneBean.setLatitudine(currentMarker.getPosition().getLatitude());
                segnalazioneBean.setLongitudine(currentMarker.getPosition().getLongitude());
            }

            UtenteBean utenteBean = loginController.getUtente();
            segnalazioneBean.setIdUtente(utenteBean.getIdUtente());

            // Send report
            effettuaSegnalazioneController.inviaSegnalazione(segnalazioneBean);
            showAlert("Successo", "Segnalazione inviata con successo!");

        } catch (SegnalazioneVicinaException e) {
            // Gestisce l’eccezione specifica per la segnalazione vicina
            showAlert("Attenzione", "Esiste già una segnalazione nelle vicinanze!");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Errore", "Si è verificato un errore durante l'invio della segnalazione.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static EffettuaSegnalazioneViewController getInstance() {
        return instance;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}


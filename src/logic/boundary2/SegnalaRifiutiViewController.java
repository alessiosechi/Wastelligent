package logic.boundary2;

import com.sothawo.mapjfx.*;
import com.sothawo.mapjfx.event.MapViewEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import logic.beans.CoordinateBean;
import logic.beans.LocationRequestBean;
import logic.beans.SegnalazioneBean;
import logic.controller.EffettuaSegnalazioneController;
import logic.exceptions.SegnalazioneVicinaException;

import java.io.File;

public class SegnalaRifiutiViewController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField searchField;

    @FXML
    private Pane mapPane;

    @FXML
    private Button attachImageButton;

    @FXML
    private ImageView imageView;
    
    @FXML
    private ScrollPane imageScrollPane;

    @FXML
    private Button submitButton;

    private MapView mapView;
    private Marker currentMarker;

    private EffettuaSegnalazioneController effettuaSegnalazioneController = EffettuaSegnalazioneController.getInstance();

	@FXML
	public void initialize() {

		AnchorPane sidebar = SidebarLoader.loadSidebar(SidebarType.UTENTE_BASE_SIDEBAR);
		rootPane.setLeft(sidebar);
		
		

		initMapView();

		initButtonActions();

	}

    private void initMapView() {
        mapView = new MapView();
        mapView.initialize(Configuration.builder().projection(Projection.WEB_MERCATOR).showZoomControls(true).build());

        mapView.setZoom(12); // Zoom iniziale
        mapView.setCenter(new Coordinate(41.9028, 12.4964)); // Coordinate iniziali (Roma)

        // Aggiungo la mappa al Pane
        mapPane.getChildren().add(mapView);
        mapView.setPrefWidth(mapPane.getPrefWidth());
        mapView.setPrefHeight(mapPane.getPrefHeight());

        // Evento click sulla mappa
        mapView.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
            Coordinate coordinate = event.getCoordinate().normalize();
            placeMarker(coordinate);
        });
    }

    private void initButtonActions() {
    	imageView.setDisable(true); // Disabilita l'ImageView

    	attachImageButton.setOnAction(event -> {
    	    FileChooser fileChooser = new FileChooser();
    	    fileChooser.getExtensionFilters().add(
    	            new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg")
    	    );
    	    File selectedFile = fileChooser.showOpenDialog(null);
    	    if (selectedFile != null) {
//    	        imageView.setImage(new Image(selectedFile.toURI().toString()));
    	        Image image = new Image(selectedFile.toURI().toString());
    	        configureImageView(image); 
    	    }
    	});
        searchField.setOnAction(event -> {
            String searchQuery = searchField.getText();
            if (!searchQuery.isEmpty()) {

				CoordinateBean posizioneBean = effettuaSegnalazioneController
						.getCoordinates(new LocationRequestBean(searchQuery));
				Coordinate coordinate = new Coordinate(posizioneBean.getLatitudine(),
						posizioneBean.getLongitudine());

				mapView.setCenter(coordinate);
				placeMarker(coordinate);
            } else {
                showAlert("Errore", "La ricerca non può essere vuota.");
            }
        });

        // Azione per il pulsante "Invia segnalazione"
        submitButton.setOnAction(event -> {
            String description = descriptionField.getText();
            String photoPath = (imageView.getImage() != null) ? imageView.getImage().getUrl() : null;
            boolean success = inviaSegnalazione(description, photoPath);
            if (success) {
                clearFields();
            }
        });
    }
    
    private void configureImageView(Image image) {

        
	    imageView.setImage(image);
	    imageView.setFitWidth(image.getWidth());
	    imageView.setFitHeight(image.getHeight());

        // Mantenere il rapporto di aspetto dell'immagine
        imageView.setPreserveRatio(true);
        imageScrollPane.setContent(imageView);
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

            // Uso la posizione del marker corrente
            if (currentMarker != null) {
                segnalazioneBean.setLatitudine(currentMarker.getPosition().getLatitude());
                segnalazioneBean.setLongitudine(currentMarker.getPosition().getLongitude());
            }

            // Invio della segnalazione
            effettuaSegnalazioneController.inviaSegnalazione(segnalazioneBean);

            // Mostro messaggio di successo
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

    private void clearFields() {
        descriptionField.clear();
        imageView.setImage(null);
        if (currentMarker != null) {
            mapView.removeMarker(currentMarker);
            currentMarker = null;
        }
        mapView.setCenter(new Coordinate(41.9028, 12.4964));
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

package logic.boundary2;

import com.sothawo.mapjfx.*;
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

	private EffettuaSegnalazioneController effettuaSegnalazioneController = new EffettuaSegnalazioneController();
	
	
	
	/**
	 * Differenze principali tra SegnalaRifiutiView e EffettuaSegnalazioneView:
	 * - al posto della barra del menu, viene utilizzata una sidebar, ma è definita in un file FXML separato e caricata dinamicamente;
	 * - non è presente un button per cercare la posizione: dopo averla inserita, bisogna premere Invio;
	 * - il layout usa un BorderPane con un AnchorPane al centro, invece di un AnchorPane come contenitore principale;
	 * - i campi di testo sono TextField anziché TextArea;
	 * - l'immagine allegata viene visualizzata in uno ScrollPane con ImageView, senza un TextArea per inserire manualmente il percorso;
	 * - la posizione può essere impostata solo tramite ricerca nel TextField, senza possibilità di aggiungere un marker manualmente sulla mappa;
	 */


	@FXML
	public void initialize() {

		AnchorPane sidebar = SidebarLoader.caricaSidebar(SidebarType.UTENTE_BASE_SIDEBAR);
		rootPane.setLeft(sidebar);

		inizializzaMapView();

		configuraPulsanti();

	}

	private void inizializzaMapView() {
		mapView = new MapView();
		mapView.initialize(Configuration.builder().projection(Projection.WEB_MERCATOR).showZoomControls(true).build());

		mapView.setZoom(12);
		mapView.setCenter(new Coordinate(41.9028, 12.4964));

		mapPane.getChildren().add(mapView);
		mapView.setPrefWidth(mapPane.getPrefWidth());
		mapView.setPrefHeight(mapPane.getPrefHeight());
	}

	private void configuraPulsanti() {
		imageView.setDisable(true);

		attachImageButton.setOnAction(event -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters()
					.add(new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg"));
			File selectedFile = fileChooser.showOpenDialog(null);
			if (selectedFile != null) {
				Image image = new Image(selectedFile.toURI().toString());
				configuraImageView(image);
			}
		});
		searchField.setOnAction(event -> {
			String searchQuery = searchField.getText();
			if (!searchQuery.isEmpty()) {

				CoordinateBean posizioneBean = effettuaSegnalazioneController
						.getCoordinates(new LocationRequestBean(searchQuery));
				Coordinate coordinate = new Coordinate(posizioneBean.getLatitudine(), posizioneBean.getLongitudine());

				mapView.setCenter(coordinate);
				inserisciMarker(coordinate);
			} else {
				showAlert("Errore", "La ricerca non può essere vuota.");
			}
		});

		submitButton.setOnAction(event -> {
			String description = descriptionField.getText();
			String photoPath = (imageView.getImage() != null) ? imageView.getImage().getUrl() : null;
			boolean success = inviaSegnalazione(description, photoPath);
			if (success) {
				resettaCampi();
			}
		});
	}

	private void configuraImageView(Image image) {

		imageView.setImage(image);
		imageView.setFitWidth(image.getWidth());
		imageView.setFitHeight(image.getHeight());

		imageView.setPreserveRatio(true);
		imageScrollPane.setContent(imageView);
	}

	private void inserisciMarker(Coordinate coordinate) {
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

			if (currentMarker != null) {
				segnalazioneBean.setLatitudine(currentMarker.getPosition().getLatitude());
				segnalazioneBean.setLongitudine(currentMarker.getPosition().getLongitude());
			}

			effettuaSegnalazioneController.inviaSegnalazione(segnalazioneBean);

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

	private void resettaCampi() {
		descriptionField.clear();
		imageView.setImage(null);
		searchField.clear();
		searchField.setPromptText("Cerca un luogo");
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

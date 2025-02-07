package logic.boundary2;

import com.sothawo.mapjfx.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DettagliSegnalazione {

	private DettagliSegnalazione() {
	}

	public static void mostraFoto(String fotoPercorso) {
		if (fotoPercorso != null && !fotoPercorso.isEmpty()) {
			Stage popupStage = new Stage(); // creo la finestra
			VBox vbox = new VBox(10);
			ImageView imageView = new ImageView();

			try {
				Image image = new Image("file:" + fotoPercorso);
				imageView.setImage(image);
				imageView.setFitWidth(400);
				imageView.setPreserveRatio(true);

				vbox.getChildren().add(imageView);
				Scene scene = new Scene(vbox, 600, 400);
				popupStage.setTitle("Foto della Segnalazione");
				popupStage.setScene(scene);
				popupStage.show();
			} catch (Exception e) {
				showAlert(Alert.AlertType.ERROR, "Errore", "Impossibile caricare l'immagine.");
			}
		} else {
			showAlert(Alert.AlertType.WARNING, "Attenzione", "Nessuna foto disponibile per questa segnalazione.");
		}
	}

	public static void mostraMappa(double latitudine, double longitudine) {
		Stage popupStage = new Stage();
		VBox vbox = new VBox(10);
		vbox.setPadding(new Insets(10));

		MapView mapView = new MapView();
		mapView.setPrefSize(600, 400);

		Configuration config = Configuration.builder().projection(Projection.WEB_MERCATOR).showZoomControls(true)
				.build();

		mapView.initialize(config);

		mapView.initializedProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) { // quando la mappa è inizializzata 
				Coordinate coordinate = new Coordinate(latitudine, longitudine);
				mapView.setCenter(coordinate);
				mapView.setZoom(15);

				Marker marker = Marker.createProvided(Marker.Provided.RED).setPosition(coordinate).setVisible(true);
				mapView.addMarker(marker);

				Platform.runLater(mapView::requestLayout); // forzo l'aggiornamento del layout
			}
		});

		vbox.getChildren().add(mapView);
		Scene scene = new Scene(vbox, 600, 400);
		popupStage.setTitle("Posizione della Segnalazione");
		popupStage.setScene(scene);
		popupStage.show();
	}

	private static void showAlert(Alert.AlertType type, String title, String message) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}
}

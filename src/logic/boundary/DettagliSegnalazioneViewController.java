package logic.boundary;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.MapView;

import java.util.logging.Logger;

import com.sothawo.mapjfx.Configuration;
import com.sothawo.mapjfx.Projection;
import com.sothawo.mapjfx.event.MapViewEvent;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.beans.SegnalazioneBean;
import logic.boundary.components.ViewInfo;
import logic.boundary.components.ViewLoader;


public class DettagliSegnalazioneViewController {
	
    @FXML 
    private Button indietroButton;
    @FXML 
    private Button button1;
    @FXML 
    private Button button2;
    @FXML
    private Line line;
    @FXML 
    private Button exitButton;
    @FXML 
    private ImageView imageView;
    @FXML 
    private ScrollPane imageScrollPane;
    @FXML 
    private Label posizioneLabel;
    @FXML 
    private Pane mapPane;


    private static DettagliSegnalazioneViewController instance;
	private static final Logger logger = Logger.getLogger(DettagliSegnalazioneViewController.class.getName());
    private CallerType callerType;
    private SegnalazioneBean segnalazioneBean; 
    
    
    
    public void setCallerType(CallerType callerType) {
        this.callerType = callerType;

    }
    private void configuraView() {
        switch (callerType) {
            case CONTROLLER1:
				indietroButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.ASSEGNA_PUNTI_VIEW));
				button1.setOnAction(event -> ViewLoader.caricaView(ViewInfo.GESTISCI_SEGNALAZIONI_VIEW));
				button2.setOnAction(event -> ViewLoader.caricaView(ViewInfo.ASSEGNA_PUNTI_VIEW));
				button1.setText("GESTISCI\nSEGNALAZIONI");
				button2.setText("ASSEGNA\nPUNTI");
				line.setLayoutX(645.0);
                break;
            case CONTROLLER2:
				indietroButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.GESTISCI_SEGNALAZIONI_VIEW));
				button1.setOnAction(event -> ViewLoader.caricaView(ViewInfo.GESTISCI_SEGNALAZIONI_VIEW));
				button2.setOnAction(event -> ViewLoader.caricaView(ViewInfo.ASSEGNA_PUNTI_VIEW));
				button1.setText("GESTISCI\nSEGNALAZIONI");
				button2.setText("ASSEGNA\nPUNTI");
				line.setLayoutX(429.75);
                break;
            case CONTROLLER3:
				indietroButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.SEGNALAZIONI_ASSEGNATE_VIEW));
				button2.setDisable(true);
				button2.setVisible(false);
				button1.setLayoutX(537.375);
				line.setLayoutX(537.375);
				button1.setText("VISUALIZZA\nASSEGNAZIONI");
				button1.setOnAction(event -> ViewLoader.caricaView(ViewInfo.SEGNALAZIONI_ASSEGNATE_VIEW));
                break;
            case CONTROLLER4:
				indietroButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.STORICO_VIEW));
				button1.setOnAction(event -> ViewLoader.caricaView(ViewInfo.EFFETTUA_SEGNALAZIONE_VIEW));
				button2.setOnAction(event -> ViewLoader.caricaView(ViewInfo.RISCATTA_RICOMPENSA_VIEW));
				button1.setText("NUOVA\nSEGNALAZIONE");
				button2.setText("RISCATTA\nRICOMPENSA");
				line.setLayoutX(645.0);
                break;
        }
    }

	@FXML
	public void initialize() {
        configuraView();
	    caricaImmagine();
	    configuraMapView();
	    configuraLabel();
		exitButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.LOGIN_VIEW));
	}
	private void caricaImmagine() {
	    if (segnalazioneBean != null && segnalazioneBean.getFoto() != null) {
	        String imagePath = "file:/" + segnalazioneBean.getFoto().replace("\\", "/");
	        try {
	            Image image = new Image(imagePath);
	            configuraImageView(image);
	            configuraImageViewTooltip();
	            configuraImageViewDoubleClick(image);
	        } catch (Exception e) {
	            logger.info("Errore durante il caricamento dell'immagine: " + e.getMessage());
	        }
	    }
	}
	
	
	private void configuraImageView(Image image) {
	    imageView.setImage(image);
	    imageView.setFitWidth(image.getWidth());
	    imageView.setFitHeight(image.getHeight());
	    imageScrollPane.setContent(imageView);
	}

	private void configuraImageViewTooltip() {
	    Tooltip tooltip = new Tooltip("Fai doppio click per vedere l'immagine intera");
	    tooltip.setShowDelay(Duration.millis(200));
	    Tooltip.install(imageView, tooltip);
	}

	private void configuraImageViewDoubleClick(Image image) {
	    imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	        if (event.getClickCount() == 2) {
	        	mostraPopupImmagineIntera(image);
	        }
	    });
	}
	
	
	private void configuraMapView() {
	    MapView mapView = new MapView();
	    mapView.initialize(Configuration.builder().projection(Projection.WEB_MERCATOR).showZoomControls(true).build());
	    mapPane.getChildren().add(mapView);
	    mapView.setPrefWidth(mapPane.getPrefWidth());
	    mapView.setPrefHeight(mapPane.getPrefHeight());

	    mapView.initializedProperty().addListener((observable, oldValue, newValue) -> {
	        if (newValue != null) {
	            logger.info("Mappa pronta");
				if (segnalazioneBean != null && segnalazioneBean.getLatitudine() != 0
						&& segnalazioneBean.getLongitudine() != 0) {
					Coordinate coordinate = new Coordinate(segnalazioneBean.getLatitudine(),segnalazioneBean.getLongitudine());
					mapView.setCenter(coordinate);
					mapView.setZoom(15);

					Marker marker;
					marker = Marker.createProvided(Marker.Provided.RED).setPosition(coordinate).setVisible(true);
					mapView.addMarker(marker);

					mapView.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
						if (marker != null) {
							mapView.addMarker(marker);
						}
					});
				}
	        }
	    });
	}
	private void configuraLabel() {
	    String testoPosizione = segnalazioneBean.getPosizione();
	    posizioneLabel.setText(testoPosizione);

	    Tooltip tooltip = new Tooltip(testoPosizione);
	    Tooltip.install(posizioneLabel, tooltip);
	}


    private void mostraPopupImmagineIntera(Image image) {
        ImageView fullImageView = new ImageView(image);
        StackPane root = new StackPane(fullImageView);
        Scene scene = new Scene(root);
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle("Visualizza immagine intera");
        popupStage.setScene(scene);
        popupStage.setWidth(image.getWidth());
        popupStage.setHeight(image.getHeight());
        popupStage.showAndWait();
    }

    public static DettagliSegnalazioneViewController getInstance() {
        if (instance == null) {
            instance = new DettagliSegnalazioneViewController();
        }
        return instance;
    }
    
    public void setSegnalazioneBean(SegnalazioneBean segnalazioneBean) {
        this.segnalazioneBean = segnalazioneBean;
    }

}

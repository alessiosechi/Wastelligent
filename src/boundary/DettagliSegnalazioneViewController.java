package boundary;

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
import model.domain.SegnalazioneBean;


public class DettagliSegnalazioneViewController {

    @FXML 
    protected Button indietroButton;
    @FXML 
    protected Button button1;
    @FXML 
    protected Button button2;
    @FXML
    protected Line line;
    
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


    private Marker marker;
    private Stage primaryStage;

    private static DettagliSegnalazioneViewController instance;
    private SegnalazioneBean segnalazioneBean;
    private StateMachine stateMachine;
    private State currentState;
	private static final Logger logger = Logger.getLogger(DettagliSegnalazioneViewController.class.getName());


//	public DettagliSegnalazioneViewController() {
//		instance = this;
//	}
	@FXML
	public void initialize() {
	    stateMachine = new StateMachine();

	    if (stateMachine != null) {
	        stateMachine.setState(currentState);
	    }

	    loadImage();
	    setupMapView();
	    setupPositionLabel();
		exitButton.setOnAction(event -> ViewLoader.caricaView("LoginView.fxml", primaryStage));
	}
	private void loadImage() {
	    if (segnalazioneBean != null && segnalazioneBean.getFoto() != null) {
	        String imagePath = "file:/" + segnalazioneBean.getFoto().replace("\\", "/");
	        try {
	            Image image = new Image(imagePath);
	            configureImageView(image);
	            setupImageViewTooltip();
	            setupImageViewDoubleClick(image);
	        } catch (Exception e) {
	            logger.info("Errore durante il caricamento dell'immagine: " + e.getMessage());
	        }
	    }
	}
	
	
	private void configureImageView(Image image) {
	    imageView.setImage(image);
	    imageView.setFitWidth(image.getWidth());
	    imageView.setFitHeight(image.getHeight());
	    imageScrollPane.setContent(imageView);
	}

	private void setupImageViewTooltip() {
	    Tooltip tooltip = new Tooltip("Fai doppio click per vedere l'immagine intera");
	    tooltip.setShowDelay(Duration.millis(200));
	    Tooltip.install(imageView, tooltip);
	}

	private void setupImageViewDoubleClick(Image image) {
	    imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
	        if (event.getClickCount() == 2) {
	            showFullImagePopup(image);
	        }
	    });
	}
	
	
	private void setupMapView() {
	    MapView mapView = new MapView();
	    mapView.initialize(Configuration.builder().projection(Projection.WEB_MERCATOR).showZoomControls(true).build());
	    mapPane.getChildren().add(mapView);
	    mapView.setPrefWidth(mapPane.getPrefWidth());
	    mapView.setPrefHeight(mapPane.getPrefHeight());

	    mapView.initializedProperty().addListener((observable, oldValue, newValue) -> {
	        if (newValue) {
	            logger.info("Mappa pronta");
				if (segnalazioneBean != null && segnalazioneBean.getLatitudine() != 0
						&& segnalazioneBean.getLongitudine() != 0) {
					Coordinate coordinate = new Coordinate(segnalazioneBean.getLatitudine(),
							segnalazioneBean.getLongitudine());
					mapView.setCenter(coordinate);
					mapView.setZoom(15);

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
	private void setupPositionLabel() {
	    String testoPosizione = segnalazioneBean.getPosizione();
	    posizioneLabel.setText(testoPosizione);

	    Tooltip tooltip = new Tooltip(testoPosizione);
	    Tooltip.install(posizioneLabel, tooltip);
	}


    private void showFullImagePopup(Image image) {
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
    
    public void setState(State state) {
        this.currentState = state;  // salvo lo stato
        if (stateMachine != null) {
            stateMachine.setState(state);  // imposto lo stato nella state machine
        }
    }
    
    
    // getter per recuperare la segnalazione selezionata
    public SegnalazioneBean getSegnalazioneBean() {
        return segnalazioneBean;
    }

    // setter per impostare la segnalazione selezionata
    public void setSegnalazioneBean(SegnalazioneBean segnalazioneBean) {
        this.segnalazioneBean = segnalazioneBean;
    }
    
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}

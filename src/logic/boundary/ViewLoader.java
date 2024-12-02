package logic.boundary;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class ViewLoader {
	private static final Logger logger = Logger.getLogger(ViewLoader.class.getName());
    private static Stage stage;

	private ViewLoader() {
	}
		
    public static void setStage(Stage primaryStage) {
        if (stage == null) {
        	stage = primaryStage;
        } else {
            logger.warning("Stage già impostato");
        }
    }
	public static void caricaView(ViewInfo viewInfo) {

		try {
			FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(viewInfo.getFxmlPath()));
			
	        if (Objects.equals(viewInfo, ViewInfo.DETTAGLI_VIEW)) {
	            loader.setController(DettagliSegnalazioneViewController.getInstance());
	        }
	        
//	        if (Objects.equals(viewInfo, ViewInfo.LOGIN_VIEW)) {
//		        stage.hide();
//	        }
//			

			Parent root = loader.load();


			stage.setScene(new Scene(root));
			stage.centerOnScreen();
			stage.setTitle(viewInfo.getTitle());
			stage.show();
		} catch (IOException e) {
			logger.severe("Errore durante il caricamento della view: " + e.getMessage());
		}
	}	
	
}

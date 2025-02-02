package logic.boundary.components;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logic.beans.SegnalazioneBean;
import logic.boundary.CallerType;
import logic.boundary.DettagliSegnalazioneViewController;

import java.io.IOException;
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

			Parent root = loader.load();
			stage.setScene(new Scene(root));
			stage.setTitle(viewInfo.getTitle());
			stage.show();
		} catch (IOException e) {
			logger.severe("Errore durante il caricamento della view: " + e.getMessage());
		}
	}
	
	
	public static void caricaDettagliSegnalazioneView(SegnalazioneBean segnalazioneBean, CallerType callerType) {
		try {

			FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(ViewInfo.DETTAGLI_VIEW.getFxmlPath()));

			Parent root = loader.load();
			DettagliSegnalazioneViewController controller = loader.getController();

			controller.setCallerType(callerType);
			controller.setSegnalazioneBean(segnalazioneBean);
			controller.configurazione();

			stage.setScene(new Scene(root));
			stage.setTitle(ViewInfo.DETTAGLI_VIEW.getTitle());
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}

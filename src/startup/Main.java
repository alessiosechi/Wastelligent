package startup;

import javafx.application.Application;
import javafx.stage.Stage;
import logic.boundary.components.ViewInfo;
import logic.boundary.components.ViewLoader;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setResizable(false); // non si può ridimensionare la finestra

		ViewLoader.setStage(primaryStage);
		ViewLoader.caricaView(ViewInfo.CONFIGURATION_VIEW);
	}
	

	public static void main(String[] args) {
		launch(args);
	}
	

}

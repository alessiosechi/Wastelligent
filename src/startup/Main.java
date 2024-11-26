package startup;

import javafx.application.Application;
import javafx.stage.Stage;
import logic.boundary.ViewInfo;
import logic.boundary.ViewLoader;
import logic.config.PersistenceConfigurator;
import logic.config.PersistenceProvider;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false); // non si può ridimensionare la finestra
        PersistenceConfigurator.configurePersistence(PersistenceProvider.DATABASE); 
        
        ViewLoader.setStage(primaryStage);   
        ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);   
    }

    public static void main(String[] args) {
        launch(args);
    }
}

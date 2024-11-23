package startup;

import boundary.ViewInfo;
import boundary.ViewLoader;
import config.PersistenceConfigurator;
import config.PersistenceProvider;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setResizable(false); // non si può ridimensionare la finestra
        PersistenceConfigurator.configurePersistence(PersistenceProvider.IN_MEMORY); 
        
        ViewLoader.setStage(primaryStage);   
        ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);   
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package boundary;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class ViewLoader {
	private static final Logger logger = Logger.getLogger(ViewLoader.class.getName());
    private static ControllerGraficoFactory controllerGraficoFactory = new ControllerGraficoFactory();	


	

    private ViewLoader() {
        /* 
         * Costruttore privato per nascondere il costruttore pubblico predefinito.
         * La classe ViewLoader contiene solo metodi e variabili static, infatti è una classe di utility e non 
         * dovrebbe essere istanziata.
         */
    }


    public static void caricaView(ViewInfo viewInfo, Stage stage) {
        try {
            // carico la nuova view
            FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(viewInfo.getFxmlPath()));

            // ottengo il controller grafico associato alla view tramite reflection
            Object controller = controllerGraficoFactory.createController(viewInfo);
            loader.setController(controller);
  
            Parent root = loader.load();

            setPrimaryStageIfExists(controller, stage);
            

            // imposto la nuova scena nello stage
            stage.setScene(new Scene(root));       
            stage.setTitle(viewInfo.getTitle());

            stage.show();
        } catch (IOException | IllegalArgumentException  e) {
            logger.severe("Errore durante il caricamento della view: "+e.getMessage());
        }
    }
    

    
    
    
    private static void setPrimaryStageIfExists(Object controller, Stage stage) {
        // tramite reflection chiamo il metodo "setPrimaryStage" (se esiste nel controller)
        if (controller != null) {
            try {
                // Ottengo il metodo setPrimaryStage
                Method setPrimaryStageMethod = controller.getClass().getMethod("setPrimaryStage", Stage.class);

                // Passo lo stage al metodo
                setPrimaryStageMethod.invoke(controller, stage);
            } catch (NoSuchMethodException e) {
                logger.info("Il controller non ha il metodo setPrimaryStage: " + e.getMessage());
            } catch (Exception e) {
                logger.severe("Si è verificato un errore: "+e.getMessage());
            }
        }
    }
}

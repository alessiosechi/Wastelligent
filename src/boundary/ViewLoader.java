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

    private ViewLoader() {}

    // Metodo generico per caricare dinamicamente una view specifica
    public static void caricaView(ViewInfo viewInfo, Stage stage) {
        // Ottieni il controller corrispondente
        Object controller = getControllerInstance(viewInfo);
        if (controller != null) {
            caricaView(viewInfo.getFxmlPath(), viewInfo.getTitle(), stage, controller);
        } else {
            logger.severe("Controller non trovato per la view: " + viewInfo);
        }
    }

    // Metodo per caricare la view con FXML, titolo e controller specifico
    private static void caricaView(String fxmlPath, String title, Stage stage, Object controller) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewLoader.class.getResource(fxmlPath));
            loader.setController(controller);
            Parent root = loader.load();
            setPrimaryStageIfExists(controller, stage);
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            logger.severe("Errore durante il caricamento della view: " + e.getMessage());
        }
    }

    // Metodo per ottenere il controller singleton associato a ogni view
    private static Object getControllerInstance(ViewInfo viewInfo) {
        switch (viewInfo) {
            case LOGIN_VIEW:
                return LoginViewController.getInstance();
            case REGISTRAZIONE_VIEW:
                return RegistrazioneViewController.getInstance();
            case EFFETTUA_SEGNALAZIONE_VIEW:
                return EffettuaSegnalazioneViewController.getInstance();
            case RISCATTA_RICOMPENSA_VIEW:
                return RiscattaRicompensaViewController.getInstance();
            case STORICO_VIEW:
                return StoricoViewController.getInstance();
            case GESTISCI_SEGNALAZIONI_VIEW:
                return GestisciSegnalazioniViewController.getInstance();
            case ASSEGNA_PUNTI_VIEW:
                return AssegnaPuntiViewController.getInstance();
            case SEGNALAZIONI_ASSEGNATE_VIEW:
                return SegnalazioniAssegnateViewController.getInstance();
            case DETTAGLI_VIEW:
                return DettagliSegnalazioneViewController.getInstance();
            default:
                return null;
        }
    }

    private static void setPrimaryStageIfExists(Object controller, Stage stage) {
        if (controller != null) {
            try {
                Method setPrimaryStageMethod = controller.getClass().getMethod("setPrimaryStage", Stage.class);
                setPrimaryStageMethod.invoke(controller, stage);
            } catch (NoSuchMethodException e) {
                logger.info("Il controller non ha il metodo setPrimaryStage.");
            } catch (Exception e) {
                logger.severe("Si è verificato un errore: " + e.getMessage());
            }
        }
    }

    

}



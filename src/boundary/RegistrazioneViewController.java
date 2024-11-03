package boundary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.domain.CredenzialiBean;
import controller.LoginController;  // Controller della logica di business
import exceptions.UsernameAlreadyTakenException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class RegistrazioneViewController {

    @FXML
    private TextField usernameField; 

    @FXML
    private PasswordField passwordField;  

    @FXML
    private Button registratiButton;  

    @FXML
    private Hyperlink loginLink; 

    private static RegistrazioneViewController instance;
    private LoginController loginController = LoginController.getInstance();  
    private Stage primaryStage;

    @FXML
    private void handleRegistratiButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert(AlertType.WARNING, "Errore", "I campi username e password sono obbligatori.");
            return;
        }


        CredenzialiBean credenzialiBean = new CredenzialiBean();
        credenzialiBean.setUsername(username);
        credenzialiBean.setPassword(password);
        
        
        
        try {
            // chiamo il controller applicativo per eseguire la registrazione
            loginController.registraUtente(credenzialiBean);

            showAlert(AlertType.INFORMATION, "Registrazione avvenuta", "La registrazione è avvenuta con successo.");
            ViewLoader.caricaView("LoginView.fxml", primaryStage);

        } catch (UsernameAlreadyTakenException e) {
            showAlert(AlertType.ERROR, "Errore di Registrazione", "Il nome utente è già in uso. Scegline un altro.");

        } catch (Exception e) {
            showAlert(AlertType.ERROR, "Errore di Registrazione", "Registrazione fallita. Riprova.");
        }
        

    }


    @FXML
    private void handleLoginLinkAction(ActionEvent event) {

        ViewLoader.caricaView("LoginView.fxml", primaryStage);
    }

    private void showAlert(AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    
    // Singleton: restituisco l'istanza di RegistrazioneViewController
    public static RegistrazioneViewController getInstance() {
        if (instance == null) {
            instance = new RegistrazioneViewController();
        }
        return instance;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}


package boundary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.domain.CredenzialiBean;

import java.io.IOException;

import controller.LoginController;

public class LoginViewController_v0 {

	private static LoginViewController_v0 instance;
	private LoginController loginController = LoginController.getInstance();

	private Stage primaryStage;

	@FXML
	private TextField usernameField;

	@FXML
	private PasswordField passwordField;


	@FXML
	private void handleLoginButtonAction(ActionEvent event) {
	    ValidaInput usernameValidatore = new ValidatoreSpaziVuoti(new ValidatoreLunghezzaMinima(new ValidatoreBase(), 5));
	    ValidaInput passwordValidatore = new ValidatoreSpaziVuoti(new ValidatoreLunghezzaMinima(new ValidatoreBase(), 8));
		String username = "utente_base1";
		String password = "password1";
	    //String username = usernameField.getText();
	    //String password = passwordField.getText();


	    if (!usernameValidatore.valida(username)) {
	        showAlert(Alert.AlertType.WARNING, "Errore Validazione", usernameValidatore.getMessaggioErrore());
	        return;
	    }

	    if (!passwordValidatore.valida(password)) {
	        showAlert(Alert.AlertType.WARNING, "Errore Validazione", passwordValidatore.getMessaggioErrore());
	        return;
	    }

	    authenticate(username, password);
	}



	private void authenticate(String username, String password) {
		CredenzialiBean credenzialiBean = new CredenzialiBean();
		credenzialiBean.setUsername(username);
		credenzialiBean.setPassword(password);
		int tipoId = loginController.effettuaLogin(credenzialiBean);

		if (tipoId == -1) {
			showAlert(Alert.AlertType.ERROR, "Login Fallito", "Nome utente o password non validi.");
			return;
		}

		// Imposta l'utente nel LoginController con l'ID del ruolo
		// LoginController.setUtente(username, tipoId);

		// Mostra un messaggio di successo e carica la view successiva
		showAlert(Alert.AlertType.INFORMATION, "Login Success", "Login effettuato con successo!");
		//String view = loginController.ottieniView(1);
		loadView(); // Metodo per caricare la nuova vista
	}

	public static LoginViewController_v0 getInstance() {
		if (instance == null) {
			instance = new LoginViewController_v0();
		}
		return instance;
	}

	private void loadView() {
		try {
			// Crea un FXMLLoader per caricare la nuova vista
			FXMLLoader loader = new FXMLLoader(getClass().getResource(loginController.ottieniView(1)));

			// Carica il nuovo layout
			Parent root = loader.load();

			// Imposta il nuovo layout nella finestra principale
			if (primaryStage != null) {
				primaryStage.setScene(new Scene(root));
				primaryStage.show();
			} else {
				showAlert(AlertType.ERROR, "Errore", "La finestra principale non è impostata.");
			}
		} catch (IOException e) {
			e.printStackTrace();
			showAlert(AlertType.ERROR, "Errore di Caricamento", "Impossibile caricare la nuova vista.");
		}
	}

	private void showAlert(AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.showAndWait();
	}

	public void setPrimaryStage(Stage primaryStage) { // PASSO LO STAGE
		this.primaryStage = primaryStage;
	}
}

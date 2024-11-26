package logic.boundary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import logic.controller.LoginController;
import logic.decorator.ValidaInput;
import logic.decorator.ValidatoreSpaziVuoti;
import logic.exceptions.RegistrazioneUtenteException;
import logic.exceptions.UsernameAlreadyTakenException;
import logic.model.domain.CredenzialiBean;
import logic.decorator.ValidatoreLunghezzaMinima;
import logic.decorator.ValidatoreBase;

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
//	private LoginController loginController = LoginController.getInstance();
//
//	@FXML
//	private void handleRegistratiButtonAction(ActionEvent event) {
//		// utilizzo il design pattern decorator
//		ValidaInput usernameValidatore = new ValidatoreSpaziVuoti(
//				new ValidatoreLunghezzaMinima(new ValidatoreBase(), 5));
//		ValidaInput passwordValidatore = new ValidatoreSpaziVuoti(
//				new ValidatoreLunghezzaMinima(new ValidatoreBase(), 8));
//
//		String username = usernameField.getText();
//		String password = passwordField.getText();
//
//		if (username.isEmpty() || password.isEmpty()) {
//			showAlert(AlertType.WARNING, "Errore", "I campi username e password sono obbligatori.");
//			return;
//		}
//
//		// se lo username non è valido, mostro un alert
//		if (!usernameValidatore.valida(username)) {
//			showAlert(Alert.AlertType.WARNING, "Errore Validazione", usernameValidatore.getMessaggioErrore());
//			return;
//		}
//
//		// se la password non è valida, mostro un alert
//		if (!passwordValidatore.valida(password)) {
//			showAlert(Alert.AlertType.WARNING, "Errore Validazione", passwordValidatore.getMessaggioErrore());
//			return;
//		}
//
//		CredenzialiBean credenzialiBean = new CredenzialiBean();
//		credenzialiBean.setUsername(username);
//		credenzialiBean.setPassword(password);
//
//		try {
//			loginController.registraUtente(credenzialiBean);
//
//			showAlert(AlertType.INFORMATION, "Registrazione avvenuta", "La registrazione è avvenuta con successo.");
//			ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
//		} catch (UsernameAlreadyTakenException | RegistrazioneUtenteException e) {
//			showAlert(AlertType.ERROR, "Errore Registrazione", e.getMessage());
//		}
//
//	}

	@FXML
	private void handleLoginLinkAction(ActionEvent event) {
		ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
	}

	private void showAlert(AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.showAndWait();
	}

	public static RegistrazioneViewController getInstance() {
		if (instance == null) {
			instance = new RegistrazioneViewController();
		}
		return instance;
	}

}

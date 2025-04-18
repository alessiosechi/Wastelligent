package logic.boundary.components;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import logic.beans.CredenzialiBean;
import logic.controller.LoginController;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Hyperlink;

public class LoginViewController {

	@FXML
	private TextField usernameField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private RadioButton interfaceOption1;

	@FXML
	private RadioButton interfaceOption2;

	@FXML
	private Hyperlink registerLink;

	private LoginController loginController = new LoginController();

	@FXML
	private void initialize() {

		ToggleGroup toggleGroup = new ToggleGroup();

		// assegno il ToggleGroup ai RadioButton in modo tale da gestire la selezione degli stessi
		interfaceOption1.setToggleGroup(toggleGroup);
		interfaceOption2.setToggleGroup(toggleGroup);

	}

	// metodo che viene eseguito al click del button "LOGIN"
	@FXML
	private void handleLoginButtonAction(ActionEvent event) {

		String username = usernameField.getText();
		String password = passwordField.getText();

		int interfacciaSelezionata = interfaceOption1.isSelected() ? 1 : 2;
		authenticate(username, password, interfacciaSelezionata);
	}

	private void authenticate(String username, String password, int interfacciaSelezionata) {
		CredenzialiBean credenzialiBean = new CredenzialiBean();
		credenzialiBean.setUsername(username);
		credenzialiBean.setPassword(password);

		int success = loginController.effettuaLogin(credenzialiBean);

		if (success == -1) {
			showAlert(Alert.AlertType.ERROR, "Login Fallito", "Nome utente e/o password non validi.");
			return;
		}

		showAlert(Alert.AlertType.INFORMATION, "Login Success", "Login effettuato con successo!");

		// carico la nuova vista dopo il login
		String viewIniziale = loginController.ottieniView(interfacciaSelezionata);
		ViewLoader.caricaView(ViewInfo.fromFxmlPath(viewIniziale));
	}

	@FXML
	private void handleRegisterLinkAction() {
		ViewLoader.caricaView(ViewInfo.REGISTRAZIONE_VIEW);
	}

	private void showAlert(AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.showAndWait();
	}

}

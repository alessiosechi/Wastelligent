package logic.boundary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import logic.beans.SignUpBean;
import logic.controller.RegistrazioneController;
import logic.decorator.ValidaInput;
import logic.decorator.ValidatoreSpaziVuoti;
import logic.exceptions.RegistrazioneUtenteException;
import logic.exceptions.UsernameAlreadyTakenException;
import logic.decorator.ValidatoreLunghezzaMinima;
import logic.decorator.ValidatoreBase;

public class RegistrazioneViewController {

	@FXML
	private TextField usernameField;

	@FXML
	private PasswordField passwordField;

	@FXML
	private ComboBox<TipologiaUtente> tipologiaUtenteComboBox;

	@FXML
	private Button registratiButton;

	@FXML
	private Hyperlink loginLink;

	private RegistrazioneController registrazioneController = RegistrazioneController.getInstance();

	@FXML
	private void initialize() {

		tipologiaUtenteComboBox.getItems().addAll(TipologiaUtente.values());
	}

	@FXML
	private void handleRegistratiButtonAction(ActionEvent event) {
		// utilizzo il design pattern decorator
		ValidaInput usernameValidatore = new ValidatoreSpaziVuoti(
				new ValidatoreLunghezzaMinima(new ValidatoreBase(), 5));
		ValidaInput passwordValidatore = new ValidatoreSpaziVuoti(
				new ValidatoreLunghezzaMinima(new ValidatoreBase(), 8));

		String username = usernameField.getText();
		String password = passwordField.getText();
		TipologiaUtente tipologia = tipologiaUtenteComboBox.getValue();

		if (username.isEmpty() || password.isEmpty()) {
			showAlert(AlertType.WARNING, "Errore", "I campi username e password sono obbligatori.");
			return;
		}

		// se lo username non è valido, mostro un alert
		if (!usernameValidatore.valida(username)) {
			showAlert(Alert.AlertType.WARNING, "Errore Validazione", usernameValidatore.getMessaggioErrore());
			return;
		}

		// se la password non è valida, mostro un alert
		if (!passwordValidatore.valida(password)) {
			showAlert(Alert.AlertType.WARNING, "Errore Validazione", passwordValidatore.getMessaggioErrore());
			return;
		}

		SignUpBean signUpBean = new SignUpBean();
		signUpBean.setUsername(username);
		signUpBean.setPassword(password);
		signUpBean.setTipologiaId(tipologia.getValore());

		try {
			registrazioneController.registraUtente(signUpBean);

			showAlert(AlertType.INFORMATION, "Registrazione avvenuta", "La registrazione è avvenuta con successo.");
			caricaLoginView();
		} catch (UsernameAlreadyTakenException | RegistrazioneUtenteException e) {
			showAlert(AlertType.ERROR, "Errore Registrazione", e.getMessage());
		}

	}

	private void caricaLoginView() {
		ViewLoader.caricaView(ViewInfo.LOGIN_VIEW);
	}

	@FXML
	private void handleLoginLinkAction(ActionEvent event) {
		caricaLoginView();
	}

	private void showAlert(AlertType alertType, String title, String content) {
		Alert alert = new Alert(alertType);
		alert.setTitle(title);
		alert.setContentText(content);
		alert.showAndWait();
	}

	enum TipologiaUtente {
		UTENTE_BASE(1, "Utente base"), ESPERTO(2, "Esperto ecologico"), OPERATORE(3, "Operatore ecologico");

		private final int valore;
		private final String descrizione;

		TipologiaUtente(int valore, String descrizione) {
			this.valore = valore;
			this.descrizione = descrizione;
		}

		public int getValore() {
			return valore;
		}

		@Override
		public String toString() {
			return descrizione;
		}
	}

}

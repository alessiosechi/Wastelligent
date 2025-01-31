package logic.model.domain;

public class EspertoEcologico extends Utente {
	public EspertoEcologico(int idUtente, String username) {
		super(idUtente, username);
	}

	@Override
	public String getViewIniziale(int interfaccia) {
		if (interfaccia == 1) {
			return "/logic/boundary/GestisciSegnalazioniView.fxml";
		} else {
			return "/logic/boundary2/AssegnaSegnalazioniView.fxml";
		}
	}

}

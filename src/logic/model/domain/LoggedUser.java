package logic.model.domain;

public class LoggedUser {

	private static LoggedUser instance;
	private Utente utente;

	private LoggedUser() {
	}

	public static LoggedUser getInstance() {
		if (instance == null) {
			instance = new LoggedUser();
		}
		return instance;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	/*
	 * in questo modo non violo la legge di Demetra (principio della conoscenza
	 * minima)
	 */

	public int getIdUtente() {
		return utente.getIdUtente();
	}

	public String getUsername() {
		return utente.getUsername();
	}
}

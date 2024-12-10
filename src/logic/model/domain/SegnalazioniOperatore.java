package logic.model.domain;

public class SegnalazioniOperatore extends SegnalazioniSubject {

	private static SegnalazioniOperatore instance;

	public static SegnalazioniOperatore getInstance() {
		if (instance == null) {
			instance = new SegnalazioniOperatore();
		}
		return instance;
	}

}

package logic.model.domain;

public class SegnalazioniAttive extends SegnalazioniSubject {

	private static SegnalazioniAttive instance;

	public static SegnalazioniAttive getInstance() {
		if (instance == null) {
			instance = new SegnalazioniAttive();
		}
		return instance;
	}

	
	
	// tutte le istanze dell'applicazione dovranno condividere lo stesso stato di SegnalazioniAttive
}

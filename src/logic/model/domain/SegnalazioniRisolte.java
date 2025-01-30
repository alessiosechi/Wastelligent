package logic.model.domain;

public class SegnalazioniRisolte extends SegnalazioniSubject {

	private static SegnalazioniRisolte instance;

	public static SegnalazioniRisolte getInstance() {
		if (instance == null) {
			instance = new SegnalazioniRisolte();
		}
		return instance;
	}
	
	
	// tutte le istanze dell'applicazione dovranno condividere lo stesso stato di SegnalazioniRisolte
	

}

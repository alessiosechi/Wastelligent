package logic.model.dao;

import java.util.HashMap;
import java.util.Map;

public class UtenteBaseDaoInMemory implements UtenteBaseDao {

	private Map<Integer, Integer> puntiUtenti; // mappa (id utente - punti)

	public UtenteBaseDaoInMemory() {
		this.puntiUtenti = new HashMap<>();
	}

	@Override
	public int estraiPunti(int idUtente) {
		return puntiUtenti.getOrDefault(idUtente, 0);
		// restituisco i punti dell'utente, se non esistono restituisco 0
	}

	@Override
	public void aggiungiPunti(int idUtente, int puntiDaAggiungere) {
		int puntiAttuali = puntiUtenti.getOrDefault(idUtente, 0);
		puntiUtenti.put(idUtente, puntiAttuali + puntiDaAggiungere);
	}

	@Override
	public void sottraiPunti(int idUtente, int puntiDaSottrarre) {
		int puntiAttuali = puntiUtenti.getOrDefault(idUtente, 0);
		puntiUtenti.put(idUtente, puntiAttuali - puntiDaSottrarre);

	}

}

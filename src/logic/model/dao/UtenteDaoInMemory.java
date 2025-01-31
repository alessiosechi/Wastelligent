package logic.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logic.model.domain.EspertoEcologico;
import logic.model.domain.OperatoreEcologico;
import logic.model.domain.Ruolo;
import logic.model.domain.Utente;
import logic.model.domain.UtenteBase;
import logic.model.domain.UtenteFactory;

public class UtenteDaoInMemory implements UtenteDao {

	private static final Map<String, Utente> utentiInMemory = new HashMap<>(); // mappo username - utente
	private static final Map<String, String> usernamePasswordMap = new HashMap<>(); // mappo username - password

	// mappo il ruolo e la classe corrispondente
	private static final Map<Ruolo, Class<? extends Utente>> ruoloToClasse = Map.of(Ruolo.UTENTE_BASE, UtenteBase.class,
			Ruolo.ESPERTO_ECOLOGICO, EspertoEcologico.class, Ruolo.OPERATORE_ECOLOGICO, OperatoreEcologico.class);

	@Override
	public boolean autenticazione(String username, String password) {
		return usernamePasswordMap.containsKey(username) && usernamePasswordMap.get(username).equals(password);
	}

	@Override
	public int getRuoloIdByUsername(String username) {
		Utente utente = utentiInMemory.get(username);
		if (utente != null) {
			return getRuoloIdByClasse(utente);
		}
		return -1;
	}

	private static Integer getRuoloIdByClasse(Utente utente) {
		String nomeClasse = utente.getClass().getSimpleName();

		for (Map.Entry<Ruolo, Class<? extends Utente>> entry : ruoloToClasse.entrySet()) {

			// confronto il nome della classe con quello nella mappa
			if (entry.getValue().getSimpleName().equals(nomeClasse)) {
				return entry.getKey().getId();
			}
		}
		return -1;
	}

	@Override
	public int getIdByUsername(String username) {

		Utente utente = utentiInMemory.get(username);
		if (utente != null) {
			return utente.getIdUtente();
		}
		return -1;
	}

	@Override
	public boolean registraUtente(String username, String password, Ruolo ruolo) {
		// controllo se lo username è già stato preso
		if (isUsernameTaken(username)) {
			return false;
		}

		UtenteFactory utenteFactory = UtenteFactory.getInstance();
		Utente nuovoUtente = utenteFactory.createUtente(utentiInMemory.size() + 1, username, ruolo);

		utentiInMemory.put(username, nuovoUtente);
		usernamePasswordMap.put(username, password);
		return true;
	}

	@Override
	public boolean isUsernameTaken(String username) {
		return utentiInMemory.containsKey(username);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Utente> List<T> getUtentiByRuolo(Ruolo ruolo) {
		List<T> utentiFiltrati = new ArrayList<>();

		// trovo la classe corrispondente al ruolo
		Class<? extends Utente> classeCorrispondente = ruoloToClasse.get(ruolo);
		if (classeCorrispondente == null) {
			throw new IllegalArgumentException("Ruolo sconosciuto: " + ruolo);
		}

		// filtro gli utenti in base alla classe
		for (Utente utente : utentiInMemory.values()) {
			if (classeCorrispondente.isInstance(utente)) {
				utentiFiltrati.add((T) utente); // cast a T
			}
		}

		return utentiFiltrati;
	}

}

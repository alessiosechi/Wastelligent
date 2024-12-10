package logic.model.dao;

import java.util.List;

import logic.model.domain.Ruolo;
import logic.model.domain.Utente;

public interface UtenteDao {
	public int autenticazione(String username, String password);

	public int getIdByUsername(String username);

	public boolean registraUtente(String username, String password);

	public boolean isUsernameTaken(String username);
	
	<T extends Utente> List<T> getUtentiByRuolo(Ruolo ruolo);
}

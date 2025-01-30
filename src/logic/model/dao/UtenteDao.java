package logic.model.dao;

import java.util.List;

import logic.model.domain.Ruolo;
import logic.model.domain.Utente;

public interface UtenteDao {
	public boolean autenticazione(String username, String password);
	
	public int getRuoloIdByUsername(String username);

	public int getIdByUsername(String username);

	public boolean registraUtente(String username, String password, Ruolo ruolo);

	public boolean isUsernameTaken(String username);
	
	<T extends Utente> List<T> getUtentiByRuolo(Ruolo ruolo);
}

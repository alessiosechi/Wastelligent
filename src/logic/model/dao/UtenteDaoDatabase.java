package logic.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import logic.model.dao.queries.UtenteQueries;
import logic.model.domain.Ruolo;
import logic.model.domain.Utente;
import logic.model.domain.UtenteFactory;

public class UtenteDaoDatabase implements UtenteDao {

	private static final Logger logger = Logger.getLogger(UtenteDaoDatabase.class.getName());

	@Override
	public boolean autenticazione(String username, String password) {

		Connection connessione = null;

		try {
			connessione = DBConnection.getConnection();
			ResultSet resultSet = UtenteQueries.login(connessione, username, password);
			return resultSet.next();

		} catch (SQLException e) {
			logger.severe("Errore durante l'autenticazione dell'utente: " + e.getMessage());
			return false;
		}

	}

	@Override
	public int getRuoloIdByUsername(String username) {
		int ruoloId = -1;
		Connection connessione = null;

		try {
			connessione = DBConnection.getConnection();
			ResultSet resultSet = UtenteQueries.getRuoloIdByUsername(connessione, username);

			if (resultSet.next()) {
				ruoloId = resultSet.getInt("id_ruolo");

			} else {
				logger.warning("Username non valido!");
			}
		} catch (SQLException e) {
			logger.severe("Errore durante l'autenticazione dell'utente: " + e.getMessage());
		}

		return ruoloId;
	}

	@Override
	public int getIdByUsername(String username) {
		int idUtente = -1;
		Connection connessione = null;

		try {
			connessione = DBConnection.getConnection();
			ResultSet resultSet = UtenteQueries.getIdByUsername(connessione, username);

			if (resultSet.next()) {
				idUtente = resultSet.getInt("id_utente");
			} else {
				logger.warning("Username non trovato!");
			}
		} catch (SQLException e) {
			logger.severe("Errore durante il recupero dell'identificativo: " + e.getMessage());
		}

		return idUtente;
	}

	@Override
	public boolean registraUtente(String username, String password, Ruolo ruolo) {
		boolean success = false;
		Connection connessione = null;
		try {
			connessione = DBConnection.getConnection();
			connessione.setAutoCommit(false);
			int righeAggiornate = UtenteQueries.registrazione(connessione, username, password, ruolo);

			if (righeAggiornate > 0) {
				connessione.commit();
				success = true;
			} else {
				connessione.rollback();
			}
		} catch (SQLException e) {
			logger.severe("Errore durante la registrazione dell'utente: " + e.getMessage());
		}

		return success;
	}

	@Override
	public boolean isUsernameTaken(String username) {
		boolean taken = false;
		Connection connessione = null;

		try {
			connessione = DBConnection.getConnection();
			ResultSet resultSet = UtenteQueries.isUsernameTaken(connessione, username);

			if (resultSet.next()) {
				taken = resultSet.getInt("username_count") > 0;
			}
		} catch (SQLException e) {
			logger.severe("Errore durante la verifica della disponibilità dello username: " + e.getMessage());
		}

		return taken;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Utente> List<T> getUtentiByRuolo(Ruolo ruolo) {
		List<T> utenti = new ArrayList<>();
		Connection connessione = null;

		try {
			connessione = DBConnection.getConnection();
			ResultSet resultSet = UtenteQueries.getUtenti(connessione, ruolo);

			while (resultSet.next()) {
				int idUtente = resultSet.getInt("id_utente");
				String username = resultSet.getString("username");

				UtenteFactory utenteFactory = UtenteFactory.getInstance();
				Utente utente = utenteFactory.createUtente(idUtente, username, ruolo);

				utenti.add((T) utente); // cast a T
			}
		} catch (SQLException e) {
			logger.severe("Errore durante il recupero degli utenti. Dettagli: " + e.getMessage());
		}

		return utenti;
	}

}

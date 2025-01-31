
package logic.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import logic.model.dao.queries.UtenteBaseQueries;

public class UtenteBaseDaoDatabase implements UtenteBaseDao {

	private static final Logger logger = Logger.getLogger(UtenteBaseDaoDatabase.class.getName());

	public int estraiPunti(int idUtente) {
		int punti = 0;
		Connection connessione = null;

		try {
			connessione = DBConnection.getConnection();
			ResultSet resultSet = UtenteBaseQueries.estraiPunti(connessione, idUtente);

			if (resultSet.next()) {
				punti = resultSet.getInt("punti");
			}

		} catch (SQLException e) {
			logger.severe("Errore durante il recupero dei punti dell'utente: " + e.getMessage());
		}

		return punti;
	}

	@Override
	public void aggiungiPunti(int idUtente, int puntiDaAggiungere) {
		Connection connessione = null;

		try {
			connessione = DBConnection.getConnection();
			connessione.setAutoCommit(false);

			int righeAggiornate = UtenteBaseQueries.aggiungiPunti(connessione, idUtente, puntiDaAggiungere);

			if (righeAggiornate > 0) {
				connessione.commit();
			} else {
				connessione.rollback();
			}

		} catch (SQLException e) {
			logger.severe("Errore durante l'operazione di aggiunta punti: " + e.getMessage());
		}
	}

	@Override
	public void sottraiPunti(int idUtente, int puntiDaSottrarre) {
		Connection connessione = null;

		try {
			connessione = DBConnection.getConnection();
			connessione.setAutoCommit(false);

			int righeAggiornate = UtenteBaseQueries.sottraiPunti(connessione, idUtente, puntiDaSottrarre);

			if (righeAggiornate > 0) {
				connessione.commit();
			} else {
				connessione.rollback();
			}

		} catch (SQLException e) {
			logger.severe("Errore durante l'operazione di sottrazione punti: " + e.getMessage());
		}
	}

}

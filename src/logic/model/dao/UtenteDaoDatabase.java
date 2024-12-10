package logic.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import logic.model.dao.queries.UtenteQueries;
import logic.model.domain.EspertoEcologico;
import logic.model.domain.OperatoreEcologico;
import logic.model.domain.Ruolo;
import logic.model.domain.Utente;
import logic.model.domain.UtenteBase;

public class UtenteDaoDatabase implements UtenteDao {
	private static volatile UtenteDaoDatabase instance;
	private static final Logger logger = Logger.getLogger(UtenteDaoDatabase.class.getName());

	private UtenteDaoDatabase() {
	}

	public static UtenteDaoDatabase getInstance() {
		UtenteDaoDatabase result = instance;

		if (instance == null) {
			synchronized (UtenteDaoDatabase.class) {
				result = instance;
				if (result == null) {
					instance = result = new UtenteDaoDatabase();
				}
			}
		}
		return result;
	}

    @Override
    public int autenticazione(String username, String password) {
        int ruoloId = -1;
	    Connection connessione = null;

        try {
	        connessione = DBConnection.getConnection();
            ResultSet resultSet = UtenteQueries.login(connessione, username, password);

            if (resultSet.next()) {
                String ruolo = resultSet.getString("nome");
                switch (ruolo) {
                    case "UTENTE_BASE":
                        ruoloId = Ruolo.UTENTE_BASE.getId();
                        break;
                    case "ESPERTO_ECOLOGICO":
                        ruoloId = Ruolo.ESPERTO_ECOLOGICO.getId();
                        break;
                    case "OPERATORE_ECOLOGICO":
                        ruoloId = Ruolo.OPERATORE_ECOLOGICO.getId();
                        break;
                    default:
                        throw new IllegalArgumentException("Ruolo sconosciuto: " + ruolo);
                }
            } else {
                logger.warning("Username e/o password non validi!");
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
    public boolean registraUtente(String username, String password) {
        boolean success = false;
	    Connection connessione = null;
        try {
	        connessione = DBConnection.getConnection();
        	connessione.setAutoCommit(false); 
            int righeAggiornate = UtenteQueries.registrazione(connessione, username, password);

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

				Utente utente = null;

				// creo l'istanza dell'utente corretta in base al ruolo
				switch (ruolo) {
				case UTENTE_BASE:
					utente = new UtenteBase(idUtente, username);
					break;
				case ESPERTO_ECOLOGICO:
					utente = new EspertoEcologico(idUtente, username);
					break;
				case OPERATORE_ECOLOGICO:
					utente = new OperatoreEcologico(idUtente, username);
					break;
				default:
					throw new SQLException("Ruolo utente sconosciuto");
				}

				utenti.add((T) utente); // cast a T
			}
		} catch (SQLException e) {
			logger.severe("Errore durante il recupero degli utenti. Dettagli: " + e.getMessage());
		}

		return utenti;
	}

	
	


}

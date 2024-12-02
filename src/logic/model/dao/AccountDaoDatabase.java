package logic.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import logic.model.domain.Ruolo;

public class AccountDaoDatabase implements AccountDao{
	private static volatile AccountDaoDatabase instance;
	private static final Logger logger = Logger.getLogger(AccountDaoDatabase.class.getName());

	private AccountDaoDatabase() {
	}
	
	public static AccountDaoDatabase getInstance() {
		AccountDaoDatabase result = instance;

		if (instance == null) {
			synchronized (AccountDaoDatabase.class) {
				result = instance;
				if (result == null) {
					instance = result = new AccountDaoDatabase();
				}
			}
		}
		return result;
	}
	
	@Override
	public int autenticazione(String username, String password) {

	    String sql = "SELECT r.nome FROM utenti u JOIN ruoli r ON u.tipo_utente = r.id_ruolo WHERE u.username = ? AND u.password_hash = ?";

	    try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {

	        stmt.setString(1, username);
	        stmt.setString(2, password);

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                String ruolo = rs.getString("nome");
	                switch (ruolo) {
	                    case "UTENTE_BASE":
	                        return Ruolo.UTENTE_BASE.getId();
	                    case "ESPERTO_ECOLOGICO":
	                        return Ruolo.ESPERTO_ECOLOGICO.getId();
	                    case "OPERATORE_ECOLOGICO":
	                        return Ruolo.OPERATORE_ECOLOGICO.getId();
	                    default:
	                        throw new IllegalArgumentException("Ruolo sconosciuto: " + ruolo);
	                }
	            } else {
                    logger.warning("Username e/o password non validi!");
	                return -1; 
	            }
	        }
	    } catch (SQLException e) {
	    	logger.severe("Errore durante l'autenticazione dell'utente: " + e.getMessage());
	        return -1;
	    } catch (Exception e) {
            logger.severe("Errore durante il processo di autenticazione: " + e.getMessage());
	        return -1;
	    }
	}
	
	@Override
	public int getIdByUsername(String username) {
	    String sql = "SELECT id_utente FROM utenti WHERE username = ?";

	    try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {

	        stmt.setString(1, username);

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("id_utente");  
	            } else {
                    logger.warning("Username non trovato!");
	                return -1; 
	            }
	        }
	    } catch (SQLException e) {
            logger.severe("Errore durante il recupero dell'identificativo: " + e.getMessage());
	        return -1; 
	    }
	}

    @Override
    public boolean registraUtente(String username, String password) {
        Connection connessione = null;
        PreparedStatement stmt = null;

        try {
            connessione = DBConnection.getConnection();
            String sql = "INSERT INTO utenti (username, password_hash, tipo_utente) VALUES (?, ?, ?)";

            stmt = connessione.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password); 
            stmt.setInt(3, Ruolo.UTENTE_BASE.getId()); 

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.severe("Errore durante la registrazione dell'utente: " + e.getMessage());
            return false;
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                logger.severe("Errore nella chiusura delle risorse: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean isUsernameTaken(String username) {
        String sql = "SELECT COUNT(*) AS username_count FROM utenti WHERE username = ?";

        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("username_count") > 0;  
                } else {
                    return false;  
                }
            }
        } catch (SQLException e) {
            logger.severe("Errore durante la verifica della disponibilità dello username: "+ e.getMessage());
            return false;  
        }
    }
 
    
}

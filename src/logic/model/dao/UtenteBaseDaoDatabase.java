package logic.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;


public class UtenteBaseDaoDatabase implements UtenteBaseDao {

    private static volatile UtenteBaseDaoDatabase instance;
    private static final Logger logger = Logger.getLogger(UtenteBaseDaoDatabase.class.getName());

    public static UtenteBaseDaoDatabase getInstance() {
        UtenteBaseDaoDatabase result = instance;

        if (result == null) {
            synchronized (UtenteBaseDaoDatabase.class) {
                result = instance;
                if (result == null) {
                    instance = result = new UtenteBaseDaoDatabase();
                }
            }
        }
        return result;
    }
    
    public int estraiPunti(int idUtente) {
        int punti = 0;
        String sql = "SELECT punti FROM punti_utenti WHERE id_utente = ?";
        
        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {
            
            stmt.setInt(1, idUtente);
            
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    punti = resultSet.getInt("punti");
                }
            }
            
        } catch (SQLException e) {
        	logger.severe("Errore durante il recupero dei punti dell'utente: " + e.getMessage());
        }
        
        return punti;
    }

    @Override
    public void aggiungiPunti(int idUtente, int puntiDaAggiungere) {
        Connection connessione = null;
        PreparedStatement stmt = null;

        try {
            connessione = DBConnection.getConnection();
            String sql = "UPDATE punti_utenti SET punti = punti + ? WHERE id_utente = ?";
            stmt = connessione.prepareStatement(sql);
            stmt.setInt(1, puntiDaAggiungere);
            stmt.setInt(2, idUtente);

            int righeAggiornate = stmt.executeUpdate();
            if (righeAggiornate > 0) {
                connessione.commit();
            } else {
                connessione.rollback();
            }
        } catch (SQLException e) {
        	logger.severe("Errore durante l'operazione di aggiunta punti: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                logger.severe("Errore durante la chiusura del PreparedStatement dopo l'operazione di aggiunta punti. Dettagli: " + e.getMessage());
            }
        }
    }

    @Override
    public void sottraiPunti(int idUtente, int puntiDaSottrarre) {
        Connection connessione = null;
        PreparedStatement stmt = null;

        try {
            connessione = DBConnection.getConnection();
            String sql = "UPDATE punti_utenti SET punti = punti - ? WHERE id_utente = ?";
            stmt = connessione.prepareStatement(sql);
            stmt.setInt(1, puntiDaSottrarre);
            stmt.setInt(2, idUtente);

            int righeAggiornate = stmt.executeUpdate();
            if (righeAggiornate > 0) {
                connessione.commit();
            } else {
                connessione.rollback();
            }
        } catch (SQLException e) {
        	logger.severe("Errore durante l'operazione di sottrazione punti: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                logger.severe("Errore durante la chiusura del PreparedStatement dopo l'operazione di sottrazione punti. Dettagli: " + e.getMessage());
            }
        }
    }
    


    

}

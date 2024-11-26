package logic.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import logic.model.domain.OperatoreEcologico;
import logic.model.domain.Ruolo;

public class UtenteDaoDatabase implements UtenteDao {

    private static volatile UtenteDaoDatabase instance;
    private static final Logger logger = Logger.getLogger(UtenteDaoDatabase.class.getName());

    public static UtenteDaoDatabase getInstance() {
        UtenteDaoDatabase result = instance;

        if (result == null) {
            synchronized (UtenteDaoDatabase.class) {
                result = instance;
                if (result == null) {
                    instance = result = new UtenteDaoDatabase();
                }
            }
        }
        return result;
    }
    
    public int estraiPuntiUtente(int idUtente) {
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
    public void aggiungiPuntiUtente(int idUtente, int puntiDaAggiungere) {
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
    public void sottraiPuntiUtente(int idUtente, int puntiDaSottrarre) {
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
    
    @Override
    public List<OperatoreEcologico> estraiOperatoriEcologici() {
        List<OperatoreEcologico> operatoriEcologici = new ArrayList<>();
        
        String sql = "SELECT id_utente, username FROM utenti WHERE tipo_utente = ?";

        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, Ruolo.OPERATORE_ECOLOGICO.getId());

            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int idUtente = resultSet.getInt("id_utente");
                    String username = resultSet.getString("username");

                    OperatoreEcologico operatore = new OperatoreEcologico(idUtente, username);
                    operatoriEcologici.add(operatore);
                }
            }
        } catch (SQLException e) {
            logger.severe("Errore durante il recupero delle informazioni degli operatori ecologici. Dettagli: " + e.getMessage());
        }

        return operatoriEcologici;
    }

    

}

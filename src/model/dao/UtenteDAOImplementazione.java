package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import model.domain.OperatoreEcologico;
import model.domain.Ruolo;

public class UtenteDAOImplementazione implements UtenteDAO {

    private static volatile UtenteDAOImplementazione instance;
    //private static final Logger logger = Logger.getLogger(UtenteDAOImplementazione.class.getName());

    public static UtenteDAOImplementazione getInstance() {
        UtenteDAOImplementazione result = instance;

        if (result == null) {
            synchronized (UtenteDAOImplementazione.class) {
                result = instance;
                if (result == null) {
                    instance = result = new UtenteDAOImplementazione();
                }
            }
        }

        return result;
    }

    @Override
    public int estraiPuntiUtente(int idUtente) {
        int punti = 0;
        Connection connessione = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            // Ottieni la connessione al database
            connessione = DBConnection.getConnection();
            String sql = "SELECT punti FROM punti_utenti WHERE id_utente = ?";
            stmt = connessione.prepareStatement(sql);
            stmt.setInt(1, idUtente);

            // Esegui la query
            resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                punti = resultSet.getInt("punti");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gestione degli errori
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (stmt != null) stmt.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return punti;
    }

    @Override
    public void aggiungiPuntiUtente(int idUtente, int puntiDaAggiungere) {
        Connection connessione = null;
        PreparedStatement stmt = null;

        try {
            // Ottieni la connessione al database
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
            e.printStackTrace(); // Gestione degli errori
        } finally {
            try {
                if (stmt != null) stmt.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sottraiPuntiUtente(int idUtente, int puntiDaSottrarre) {
        Connection connessione = null;
        PreparedStatement stmt = null;

        try {
            // Ottieni la connessione al database
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
            e.printStackTrace(); // Gestione degli errori
        } finally {
            try {
                if (stmt != null) stmt.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    
    
    
    
    @Override
    public List<OperatoreEcologico> estraiOperatoriEcologiciDisponibili(){
        List<OperatoreEcologico> operatoriEcologici = new ArrayList<>();
        Connection connessione = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;

        try {
            // Ottieni la connessione al database
            connessione = DBConnection.getConnection();

            // Query SQL per ottenere gli utenti con tipo_utente pari a 3 (operatore ecologico)
            String sql = "SELECT id_utente, username " +
                         "FROM utenti " +
                         "WHERE tipo_utente = ?";

            stmt = connessione.prepareStatement(sql);
            stmt.setInt(1, Ruolo.OPERATORE_ECOLOGICO.getId()); // Imposta tipo_utente come 3 per filtrare gli operatori ecologici

            // Esegui la query
            resultSet = stmt.executeQuery();

            // Itera attraverso i risultati e crea gli oggetti OperatoreEcologico
            while (resultSet.next()) {
                int idUtente = resultSet.getInt("id_utente");
                String username = resultSet.getString("username");

                // Crea un nuovo OperatoreEcologico e aggiungilo alla lista
                OperatoreEcologico operatore = new OperatoreEcologico(idUtente, username);
                operatoriEcologici.add(operatore);
            }
        } catch (SQLException e) {
			//logger.severe("Errore durante l'esecuzione della query: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (stmt != null) stmt.close();

            } catch (SQLException e) {
    			//logger.severe("Errore durante la chiusura dello statement: " + e.getMessage());
            }
        }

        return operatoriEcologici;
    }

}

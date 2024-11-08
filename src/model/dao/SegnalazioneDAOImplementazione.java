package model.dao;

import model.domain.Segnalazione;
import model.domain.StatoSegnalazione;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;



public class SegnalazioneDAOImplementazione implements SegnalazioneDAO {

	private static volatile SegnalazioneDAOImplementazione instance;
	private static final Logger logger = Logger.getLogger(SegnalazioneDAOImplementazione.class.getName());
    private static final String COL_DESCRIZIONE = "descrizione";
    private static final String COL_LATITUDINE = "latitudine";
    private static final String COL_LONGITUDINE = "longitudine";
    private static final String COL_STATO = "stato";
    private static final String COL_ID_SEGNALAZIONE = "id_segnalazione";
    private static final String COL_ID_UTENTE = "id_utente";
    private static final String COL_FOTO = "foto";
    private static final String COL_PUNTI = "punti";

	private SegnalazioneDAOImplementazione() {
	}

	public static SegnalazioneDAOImplementazione getInstance() {
		SegnalazioneDAOImplementazione result = instance;

		if (instance == null) {
			// blocco sincronizzato
			synchronized (SegnalazioneDAOImplementazione.class) {
				result = instance;
				if (result == null) {
					instance = result = new SegnalazioneDAOImplementazione();
				}

			}
		}

		return result;
	}

	@Override
	public void salvaSegnalazione(Segnalazione segnalazione) {
	    Connection connessione = null;
	    PreparedStatement stmt = null;

	    try {
	        connessione = DBConnection.getConnection();
			String sql = "INSERT INTO segnalazioni (" + COL_ID_UTENTE + ", " + COL_DESCRIZIONE + ", " + COL_FOTO + ", "
					+ COL_STATO + ", " + COL_LATITUDINE + ", " + COL_LONGITUDINE + ") VALUES (?, ?, ?, ?, ?, ?)";
	        stmt = connessione.prepareStatement(sql);
	        stmt.setInt(1, segnalazione.getIdUtente());
	        stmt.setString(2, segnalazione.getDescrizione());
	        
	        // Imposta il valore per il campo foto
	        if (segnalazione.getFoto() != null) {
	            stmt.setBytes(3, segnalazione.getFoto().getBytes());
	        } else {
	            stmt.setNull(3, java.sql.Types.BLOB);
	        }
	        
	        stmt.setString(4, segnalazione.getStato());
	        stmt.setDouble(5, segnalazione.getLatitudine());
	        stmt.setDouble(6, segnalazione.getLongitudine());

	        int righeInserite = stmt.executeUpdate();
	        if (righeInserite > 0) {
	            connessione.commit();
	            logger.info("Segnalazione registrata con successo");
	        } else {
	            connessione.rollback();
	            logger.info("Errore nella registrazione della segnalazione");
	        }
	        
	        /*
	         * Quando chiudo la connessione (connessione.close()), tutte le modifiche non ancora confermate (se 
	         * la connessione era in modalità di commit automatico) vengono automaticamente confermate o annullate, a 
	         * seconda della configurazione del driver e del database.
	         * 
	         * Io non chiudo la connessione, ma utilizzo la stessa, pertanto devo gestire questo aspetto.
	         */
	        
	        
	        
	        
	        
	        
	        

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
	public List<Segnalazione> trovaSegnalazioniRiscontrate(int idUtente) {
	    List<Segnalazione> segnalazioni = new ArrayList<>();
	    Connection connessione = null;
	    PreparedStatement stmt = null;
	    ResultSet resultSet = null;

	    try {
	        // Ottieni la connessione al database
	        connessione = DBConnection.getConnection();

	        // Query SQL per ottenere le segnalazioni riscontrate di uno specifico utente
	        String sql = "SELECT s." + COL_ID_SEGNALAZIONE + ", s." + COL_DESCRIZIONE + ", s.foto, s." + COL_STATO + ", s." + COL_LATITUDINE + ", s." + COL_LONGITUDINE + ", ps.punti " +
                    "FROM segnalazioni s " +
                    "LEFT JOIN punti_segnalazioni ps ON s." + COL_ID_SEGNALAZIONE + " = ps." + COL_ID_SEGNALAZIONE +
                    " WHERE s." + COL_ID_UTENTE + " = ? AND s." + COL_STATO + " = 'Riscontrata'";

	        stmt = connessione.prepareStatement(sql);
	        stmt.setInt(1, idUtente);

	        // Esegui la query
	        resultSet = stmt.executeQuery();

	        // Itera attraverso i risultati e crea gli oggetti Segnalazione
	        while (resultSet.next()) {

	            
                Segnalazione segnalazione = new Segnalazione();
                segnalazione.setDescrizione(resultSet.getString(COL_DESCRIZIONE));
                segnalazione.setFoto(resultSet.getString(COL_FOTO));
                segnalazione.setStato(resultSet.getString(COL_STATO));
                segnalazione.setLatitudine(resultSet.getDouble(COL_LATITUDINE));
                segnalazione.setLongitudine(resultSet.getDouble(COL_LONGITUDINE));
	            // Aggiungi i punti assegnati all'oggetto Segnalazione
                int puntiAssegnati = resultSet.getInt(COL_PUNTI);
	            // Qui puoi memorizzare i punti nell'oggetto Segnalazione, se necessario
	            // segnalazione.setPuntiAssegnati(puntiAssegnati); // Assicurati di avere il setter nel tuo modello
                segnalazione.setPuntiAssegnati(puntiAssegnati);
                segnalazioni.add(segnalazione);
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

	    return segnalazioni;
	}

	
	@Override
	public List<Segnalazione> getSegnalazioni()  {
	    List<Segnalazione> segnalazioni = new ArrayList<>();
	    Connection connessione = null;
	    PreparedStatement stmt = null;
	    ResultSet resultSet = null;

	    try {
	        // Ottieni la connessione al database
	        connessione = DBConnection.getConnection();

	        // Query SQL per ottenere tutte le segnalazioni
			String sql = "SELECT " + COL_ID_SEGNALAZIONE + ", " + COL_ID_UTENTE + ", " + COL_DESCRIZIONE + ", "
					+ COL_FOTO + ", " + COL_STATO + ", " + COL_LATITUDINE + ", " + COL_LONGITUDINE
					+ " FROM segnalazioni";

	        stmt = connessione.prepareStatement(sql);

	        // Esegui la query
	        resultSet = stmt.executeQuery();

	        // Itera attraverso i risultati e crea gli oggetti Segnalazione
	        while (resultSet.next()) {
                Segnalazione segnalazione = new Segnalazione();
                segnalazione.setIdSegnalazione(resultSet.getInt(COL_ID_SEGNALAZIONE));
                segnalazione.setIdUtente(resultSet.getInt(COL_ID_UTENTE));
                segnalazione.setDescrizione(resultSet.getString(COL_DESCRIZIONE));
                segnalazione.setFoto(resultSet.getString(COL_FOTO));
                segnalazione.setStato(resultSet.getString(COL_STATO));
                segnalazione.setLatitudine(resultSet.getDouble(COL_LATITUDINE));
                segnalazione.setLongitudine(resultSet.getDouble(COL_LONGITUDINE));

	            // Aggiungi la segnalazione alla lista
	            segnalazioni.add(segnalazione);
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

	    return segnalazioni;
	}
	
	
	@Override
	public List<Segnalazione> getSegnalazioniByStato(String stato)  {
	    List<Segnalazione> segnalazioni = new ArrayList<>();
	    Connection connessione = null;
	    PreparedStatement stmt = null;
	    ResultSet resultSet = null;

	    try {
	        // Ottieni la connessione al database
	        connessione = DBConnection.getConnection();

	        // Query SQL per ottenere le segnalazioni con un determinato stato
	        String sql = "SELECT " + COL_ID_SEGNALAZIONE + ", " + COL_ID_UTENTE + ", " + COL_DESCRIZIONE + ", " + COL_FOTO +
                    ", " + COL_STATO + ", " + COL_LATITUDINE + ", " + COL_LONGITUDINE + " FROM segnalazioni " +
                    "WHERE " + COL_STATO + " = ?";

	        stmt = connessione.prepareStatement(sql);
	        stmt.setString(1, stato); // Imposta lo stato come parametro nella query

	        // Esegui la query
	        resultSet = stmt.executeQuery();

	        // Itera attraverso i risultati e crea gli oggetti Segnalazione
	        while (resultSet.next()) {
	            Segnalazione segnalazione = new Segnalazione();


	            segnalazione.setIdSegnalazione(resultSet.getInt(COL_ID_SEGNALAZIONE));
	            segnalazione.setIdUtente(resultSet.getInt(COL_ID_UTENTE));
	            segnalazione.setDescrizione(resultSet.getString(COL_DESCRIZIONE));
	            segnalazione.setFoto(resultSet.getString(COL_FOTO));
	            segnalazione.setStato(resultSet.getString(COL_STATO));
	            segnalazione.setLatitudine(resultSet.getDouble(COL_LATITUDINE));
	            segnalazione.setLongitudine(resultSet.getDouble(COL_LONGITUDINE));
	            segnalazioni.add(segnalazione);
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

	    return segnalazioni;
	}


	
	@Override
	public void eliminaSegnalazione(int idSegnalazione) {
	    Connection connessione = null;
	    PreparedStatement stmt = null;

	    try {
	        // Ottieni la connessione al database
	        connessione = DBConnection.getConnection();
	        
	        // Query SQL per eliminare la segnalazione tramite il suo ID
	        String sql = "DELETE FROM segnalazioni WHERE " + COL_ID_SEGNALAZIONE + " = ?";
	        
	        stmt = connessione.prepareStatement(sql);
	        stmt.setInt(1, idSegnalazione); // Imposta l'ID come parametro

	        int righeEliminate = stmt.executeUpdate();
	        if (righeEliminate > 0) {
	            connessione.commit();
	            logger.info("Segnalazione eliminata con successo");
	        } else {
	            connessione.rollback();
	            logger.info("Errore: nessuna segnalazione trovata con l'ID specificato");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (stmt != null) stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
	@Override
	public void assegnaOperatore(int idSegnalazione, int idOperatore, int idEsperto) {
	    Connection connessione = null;
	    PreparedStatement stmtAssegnazione = null;

	    try {
	        // Ottieni la connessione al database
	        connessione = DBConnection.getConnection();
	        connessione.setAutoCommit(false); // Imposta la transazione manuale

	        // Query SQL per inserire una nuova assegnazione
	        String sqlAssegnazione = "INSERT INTO assegnazioni (" + COL_ID_SEGNALAZIONE + ", id_operatore, id_esperto, assegnato_at) " +
                    "VALUES (?, ?, ?, NOW())";

	        stmtAssegnazione = connessione.prepareStatement(sqlAssegnazione);
	        stmtAssegnazione.setInt(1, idSegnalazione);
	        stmtAssegnazione.setInt(2, idOperatore);
	        stmtAssegnazione.setInt(3, idEsperto);

	        int righeInserite = stmtAssegnazione.executeUpdate();

	        if (righeInserite > 0) {

	            aggiornaStato(idSegnalazione, StatoSegnalazione.IN_CORSO.getStato());
	            connessione.commit(); // Commit della transazione
	        } else {
	            connessione.rollback(); // Rollback della transazione se l'inserimento fallisce
	        }

	    } catch (SQLException e) {
            try {
                connessione.rollback(); // Rollback in caso di errore
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
	    } finally {
	        try {
	            if (stmtAssegnazione != null) stmtAssegnazione.close();

	        } catch (SQLException e) {
	            e.printStackTrace(); // Gestione degli errori di chiusura
	        }
	    }
	}


	@Override
	public void assegnaPunti(int idSegnalazione, int punti) {
	    Connection connessione = null;
	    PreparedStatement stmtPunti = null;

	    try {
	        // Ottieni la connessione al database
	        connessione = DBConnection.getConnection();
	        connessione.setAutoCommit(false); // Imposta la transazione manuale

	        // Query SQL per inserire o aggiornare i punti per la segnalazione
	        String sqlPunti = "INSERT INTO punti_segnalazioni (" + COL_ID_SEGNALAZIONE + ", " + COL_PUNTI + ") " +
                    "VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE " + COL_PUNTI + " = ?";

	        stmtPunti = connessione.prepareStatement(sqlPunti);
	        stmtPunti.setInt(1, idSegnalazione);  // Imposta l'ID della segnalazione come parametro
	        stmtPunti.setInt(2, punti);            // Imposta i punti da assegnare come parametro
	        stmtPunti.setInt(3, punti);            // Aggiorna i punti in caso di conflitto

	        int righeInserite = stmtPunti.executeUpdate();

	        if (righeInserite > 0) {
	        	
	            // Usa `aggiornaStato` per impostare lo stato a 'Riscontrata'
	            aggiornaStato(idSegnalazione, StatoSegnalazione.RISCONTRATA.getStato());
	            connessione.commit();
	        } else {
	            connessione.rollback(); // Rollback della transazione se l'inserimento fallisce
	        }

	    } catch (SQLException e) {
            try {
                connessione.rollback(); // Rollback in caso di errore
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace(); // Gestione errore rollback
            }
	    } finally {
	        try {
	            if (stmtPunti != null) stmtPunti.close(); // Chiudi il PreparedStatement per i punti

	        } catch (SQLException e) {
	            e.printStackTrace(); // Gestione degli errori di chiusura
	        }
	    }
	}


	
	
	@Override
	public List<Segnalazione> getSegnalazioniAssegnate(int idOperatore, String stato)  {
	    List<Segnalazione> segnalazioni = new ArrayList<>();
	    Connection connessione = null;
	    PreparedStatement stmt = null;
	    ResultSet resultSet = null;

	    try {
	        // Ottieni la connessione al database
	        connessione = DBConnection.getConnection();

	        // Query SQL per ottenere le segnalazioni assegnate a un operatore specifico con stato "In corso"
	        String sql = "SELECT s." + COL_ID_SEGNALAZIONE + ", s." + COL_ID_UTENTE + ", s." + COL_DESCRIZIONE + 
                    ", s." + COL_FOTO + ", s." + COL_STATO + ", s." + COL_LATITUDINE + ", s." + COL_LONGITUDINE +
                    " FROM segnalazioni s JOIN assegnazioni a ON s." + COL_ID_SEGNALAZIONE + " = a.id_segnalazione " +
                    "WHERE a.id_operatore = ? AND s." + COL_STATO + " = ?";

	        stmt = connessione.prepareStatement(sql);
	        stmt.setInt(1, idOperatore); // Imposta l'ID dell'operatore come parametro
	        stmt.setString(2, stato);     // Imposta lo stato come parametro nella query

	        // Esegui la query
	        resultSet = stmt.executeQuery();

	        // Itera attraverso i risultati e crea gli oggetti Segnalazione
	        while (resultSet.next()) {
	            Segnalazione segnalazione = new Segnalazione();
	            segnalazione.setIdSegnalazione(resultSet.getInt(COL_ID_SEGNALAZIONE));
	            segnalazione.setIdUtente(resultSet.getInt(COL_ID_UTENTE));
	            segnalazione.setDescrizione(resultSet.getString(COL_DESCRIZIONE));
	            segnalazione.setFoto(resultSet.getString(COL_FOTO));
	            segnalazione.setStato(resultSet.getString(COL_STATO));
	            segnalazione.setLatitudine(resultSet.getDouble(COL_LATITUDINE));
	            segnalazione.setLongitudine(resultSet.getDouble(COL_LONGITUDINE));
	            segnalazioni.add(segnalazione);
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

	    return segnalazioni;
	}

	
	
	
	
	
	
	
	
	
	// aggiornare questo metodo per utilizzarlo negli altri
	
	@Override
	public void aggiornaStato(int idSegnalazione, String stato) {
	    Connection connessione = null;
	    PreparedStatement stmt = null;

	    try {
	        // Ottieni la connessione al database
	        connessione = DBConnection.getConnection();

	        // Query SQL per aggiornare lo stato della segnalazione
	        String sql = "UPDATE segnalazioni SET " + COL_STATO + " = ? WHERE " + COL_ID_SEGNALAZIONE + " = ?";


	        stmt = connessione.prepareStatement(sql);
	        stmt.setString(1, stato); // Imposta lo stato a "Risolta"
	        stmt.setInt(2, idSegnalazione); // Imposta l'ID della segnalazione come parametro

	        int righeAggiornate = stmt.executeUpdate();
	        if (righeAggiornate > 0) {
	            connessione.commit(); // Commit della transazione
	        } else {
	            connessione.rollback(); // Rollback della transazione se nessuna riga è stata aggiornata
	        }

	    } catch (SQLException e) {
	        e.printStackTrace(); // Gestione degli errori
            try {
                connessione.rollback(); // Rollback in caso di errore
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace(); // Gestione errore rollback
            }
	    } finally {
	        try {
	            if (stmt != null) stmt.close(); // chiudo il PreparedStatement
	        } catch (SQLException e) {
	            e.printStackTrace(); // Gestione degli errori di chiusura
	        }
	    }
	}

	
	
}

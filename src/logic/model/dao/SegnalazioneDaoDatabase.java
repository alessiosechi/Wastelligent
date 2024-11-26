package logic.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import logic.model.domain.Segnalazione;
import logic.model.domain.StatoSegnalazione;



public class SegnalazioneDaoDatabase implements SegnalazioneDao {

	private static volatile SegnalazioneDaoDatabase instance;
	private static final Logger logger = Logger.getLogger(SegnalazioneDaoDatabase.class.getName());
    private static final String COL_DESCRIZIONE = "descrizione";
    private static final String COL_LATITUDINE = "latitudine";
    private static final String COL_LONGITUDINE = "longitudine";
    private static final String COL_STATO = "stato";
    private static final String COL_ID_SEGNALAZIONE = "id_segnalazione";
    private static final String COL_ID_UTENTE = "id_utente";
    private static final String COL_FOTO = "foto";
    private static final String COL_PUNTI = "punti";

	private SegnalazioneDaoDatabase() {
	}

	public static SegnalazioneDaoDatabase getInstance() {
		SegnalazioneDaoDatabase result = instance;

		if (instance == null) {
			synchronized (SegnalazioneDaoDatabase.class) {
				result = instance;
				if (result == null) {
					instance = result = new SegnalazioneDaoDatabase();
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
	        
	        // imposto il valore per il campo foto
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
			logger.severe("Errore durante la registrazione della segnalazione: " + e.getMessage());
		} catch (Exception e) {
			logger.severe( e.getMessage());
	    } finally {
	        try {
	            if (stmt != null) stmt.close();
	        } catch (SQLException e) {
	        	logger.warning("Errore durante la chiusura dello statement: " + e.getMessage());
	        }
	    }
	}
	
	
	@Override
	public List<Segnalazione> trovaSegnalazioniRiscontrate(int idUtente) {
	    List<Segnalazione> segnalazioni = new ArrayList<>();
	    String sql = "SELECT s." + COL_ID_SEGNALAZIONE + ", s." + COL_DESCRIZIONE + ", s.foto, s." + COL_STATO + ", s." + COL_LATITUDINE + ", s." + COL_LONGITUDINE + ", ps.punti " +
	                 "FROM segnalazioni s " +
	                 "LEFT JOIN punti_segnalazioni ps ON s." + COL_ID_SEGNALAZIONE + " = ps." + COL_ID_SEGNALAZIONE +
	                 " WHERE s." + COL_ID_UTENTE + " = ? AND s." + COL_STATO + " = ?";

	    try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {

	        stmt.setInt(1, idUtente);
	        stmt.setString(2, StatoSegnalazione.RISCONTRATA.getStato()); 

	        try (ResultSet resultSet = stmt.executeQuery()) {
	            while (resultSet.next()) {
	                Segnalazione segnalazione = new Segnalazione();
	                segnalazione.setDescrizione(resultSet.getString(COL_DESCRIZIONE));
	                segnalazione.setFoto(resultSet.getString(COL_FOTO));
	                segnalazione.setStato(resultSet.getString(COL_STATO));
	                segnalazione.setLatitudine(resultSet.getDouble(COL_LATITUDINE));
	                segnalazione.setLongitudine(resultSet.getDouble(COL_LONGITUDINE));
	                segnalazione.setPuntiAssegnati(resultSet.getInt(COL_PUNTI));

	                segnalazioni.add(segnalazione);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        logger.severe(e.getMessage());
	    }

	    return segnalazioni;
	}


	@Override
	public List<Segnalazione> getSegnalazioniByStato(String stato)  {
	    List<Segnalazione> segnalazioni = new ArrayList<>();
	    String sql = "SELECT " + COL_ID_SEGNALAZIONE + ", " + COL_ID_UTENTE + ", " + COL_DESCRIZIONE + ", " + COL_FOTO +
	                 ", " + COL_STATO + ", " + COL_LATITUDINE + ", " + COL_LONGITUDINE + " FROM segnalazioni " +
	                 "WHERE " + COL_STATO + " = ?";

	    try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {

	        stmt.setString(1, stato); 

	        try (ResultSet resultSet = stmt.executeQuery()) {

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
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        logger.severe(e.getMessage());
	    }

	    return segnalazioni;
	}



	@Override
	public void eliminaSegnalazione(Segnalazione segnalazione) {
	    Connection connessione = null;
	    PreparedStatement stmt = null;

	    try {
	        connessione = DBConnection.getConnection();
	        
	        String sql = "DELETE FROM segnalazioni WHERE " + COL_ID_SEGNALAZIONE + " = ?";
	        
	        stmt = connessione.prepareStatement(sql);
	        stmt.setInt(1, segnalazione.getIdSegnalazione()); 

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
	public void assegnaOperatore(int idSegnalazione, int idOperatore) {
	    Connection connessione = null;
	    PreparedStatement stmtAssegnazione = null;

	    try {
	        connessione = DBConnection.getConnection();
	        connessione.setAutoCommit(false); // imposto la transazione manuale

			String sqlAssegnazione = "INSERT INTO assegnazioni (" + COL_ID_SEGNALAZIONE + ", id_operatore) "
					+ "VALUES (?, ?)";

	        stmtAssegnazione = connessione.prepareStatement(sqlAssegnazione);
	        stmtAssegnazione.setInt(1, idSegnalazione);
	        stmtAssegnazione.setInt(2, idOperatore);

	        int righeInserite = stmtAssegnazione.executeUpdate();

	        if (righeInserite > 0) {
	            aggiornaStato(idSegnalazione, StatoSegnalazione.IN_CORSO.getStato());
	            connessione.commit(); 
	        } else {
	            connessione.rollback();
	        }

	    } catch (SQLException e) {
            try {
                connessione.rollback(); 
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
	    } finally {
	        try {
	            if (stmtAssegnazione != null) stmtAssegnazione.close();
	        } catch (SQLException e) {
	            e.printStackTrace(); 
	        }
	    }
	}


	@Override
	public void assegnaPunti(int idSegnalazione, int punti) {
	    Connection connessione = null;
	    PreparedStatement stmtPunti = null;

	    try {
	        connessione = DBConnection.getConnection();
	        connessione.setAutoCommit(false);

	        String sqlPunti = "INSERT INTO punti_segnalazioni (" + COL_ID_SEGNALAZIONE + ", " + COL_PUNTI + ") " +
                    "VALUES (?, ?) " +
                    "ON DUPLICATE KEY UPDATE " + COL_PUNTI + " = ?";
	        /*
	         * se esiste già un record con lo stesso id_segnalazione, la query non inserisce 
	         * un nuovo record ma aggiorna quello esistente
	         */

	        stmtPunti = connessione.prepareStatement(sqlPunti);
	        stmtPunti.setInt(1, idSegnalazione);  
	        stmtPunti.setInt(2, punti);            
	        stmtPunti.setInt(3, punti);            

	        int righeInserite = stmtPunti.executeUpdate();

	        if (righeInserite > 0) {
	            aggiornaStato(idSegnalazione, StatoSegnalazione.RISCONTRATA.getStato());
	            connessione.commit();
	        } else {
	            connessione.rollback(); 
	        }
	    } catch (SQLException e) {
            try {
                connessione.rollback(); 
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace(); 
            }
	    } finally {
	        try {
	            if (stmtPunti != null) stmtPunti.close(); 

	        } catch (SQLException e) {
	            e.printStackTrace(); 
	        }
	    }
	}


	@Override
	public List<Segnalazione> getSegnalazioniAssegnate(int idOperatore, String stato) {
	    List<Segnalazione> segnalazioni = new ArrayList<>();
	    String sql = "SELECT s." + COL_ID_SEGNALAZIONE + ", s." + COL_ID_UTENTE + ", s." + COL_DESCRIZIONE + 
	                 ", s." + COL_FOTO + ", s." + COL_STATO + ", s." + COL_LATITUDINE + ", s." + COL_LONGITUDINE +
	                 " FROM segnalazioni s JOIN assegnazioni a ON s." + COL_ID_SEGNALAZIONE + " = a.id_segnalazione " +
	                 "WHERE a.id_operatore = ? AND s." + COL_STATO + " = ?";

	    try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {

	        stmt.setInt(1, idOperatore); 
	        stmt.setString(2, stato);  

	        try (ResultSet resultSet = stmt.executeQuery()) {

	            while (resultSet.next()) {
	                Segnalazione segnalazione = new Segnalazione();
	                segnalazione.setIdSegnalazione(resultSet.getInt(COL_ID_SEGNALAZIONE));
	                segnalazione.setIdUtente(resultSet.getInt(COL_ID_UTENTE));
	                segnalazione.setDescrizione(resultSet.getString(COL_DESCRIZIONE));
	                segnalazione.setFoto(resultSet.getString(COL_FOTO));
	                segnalazione.setStato(resultSet.getString(COL_STATO));
	                segnalazione.setLatitudine(resultSet.getDouble(COL_LATITUDINE));
	                segnalazione.setLongitudine(resultSet.getDouble(COL_LONGITUDINE));
	                segnalazione.setIdOperatore(idOperatore);

	                segnalazioni.add(segnalazione);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        logger.severe("Errore inaspettato: " + e.getMessage());
	    }

	    return segnalazioni;
	}


	
	@Override
	public void aggiornaStato(int idSegnalazione, String stato) {
	    Connection connessione = null;
	    PreparedStatement stmt = null;

	    try {
	        connessione = DBConnection.getConnection();

	        String sql = "UPDATE segnalazioni SET " + COL_STATO + " = ? WHERE " + COL_ID_SEGNALAZIONE + " = ?";

	        stmt = connessione.prepareStatement(sql);
	        stmt.setString(1, stato); 
	        stmt.setInt(2, idSegnalazione); 

	        int righeAggiornate = stmt.executeUpdate();
	        if (righeAggiornate > 0) {
	            connessione.commit(); 
	        } else {
	            connessione.rollback(); 
	        }

	    } catch (SQLException e) {
	        e.printStackTrace(); 
            try {
                connessione.rollback(); 
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace(); 
            }
	    } finally {
	        try {
	            if (stmt != null) stmt.close(); 
	        } catch (SQLException e) {
	            e.printStackTrace(); 
	        }
	    }
	}

	
	
}

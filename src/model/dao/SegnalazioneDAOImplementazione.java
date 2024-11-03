package model.dao;

import model.domain.Segnalazione;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SegnalazioneDAOImplementazione implements SegnalazioneDAO {

	private static volatile SegnalazioneDAOImplementazione instance;

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
	public void salvaSegnalazione(Segnalazione segnalazione) throws SQLException {
	    Connection connessione = null;
	    PreparedStatement stmt = null;

	    try {
	        connessione = DBConnection.getConnection();
	        String sql = "INSERT INTO segnalazioni (id_utente, descrizione, foto, stato, latitudine, longitudine) "
	                   + "VALUES (?, ?, ?, ?, ?, ?)";
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
	            System.out.println("Segnalazione registrata con successo");
	        } else {
	            connessione.rollback();
	            System.out.println("Errore nella registrazione della segnalazione");
	        }
	        
	        /*
	         * Quando chiudo la connessione (connessione.close()), tutte le modifiche non ancora confermate (se 
	         * la connessione era in modalità di commit automatico) vengono automaticamente confermate o annullate, a 
	         * seconda della configurazione del driver e del database.
	         * 
	         * Io non chiudo la connessione, ma utilizzo la stessa, pertanto devo gestire questo aspetto.
	         */
	        
	        
	        
	        
	        
	        
	        

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e; // Rilancia l'eccezione per la gestione a un livello superiore
	    } finally {
	        try {
	            if (stmt != null) stmt.close();
	            //if (connessione != null) connessione.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	
	
	
	
	@Override
	public List<Segnalazione> trovaSegnalazioniRiscontrate(int idUtente) throws SQLException {
	    List<Segnalazione> segnalazioni = new ArrayList<>();
	    Connection connessione = null;
	    PreparedStatement stmt = null;
	    ResultSet resultSet = null;

	    try {
	        // Ottieni la connessione al database
	        connessione = DBConnection.getConnection();

	        // Query SQL per ottenere le segnalazioni riscontrate di uno specifico utente
	        String sql = "SELECT s.id_segnalazione, s.descrizione, s.foto, s.stato, s.latitudine, s.longitudine, ps.punti " +
	                     "FROM segnalazioni s " +
	                     "LEFT JOIN punti_segnalazioni ps ON s.id_segnalazione = ps.id_segnalazione " +
	                     "WHERE s.id_utente = ? AND s.stato = 'Riscontrata'";

	        stmt = connessione.prepareStatement(sql);
	        stmt.setInt(1, idUtente);

	        // Esegui la query
	        resultSet = stmt.executeQuery();

	        // Itera attraverso i risultati e crea gli oggetti Segnalazione
	        while (resultSet.next()) {
	            Segnalazione segnalazione = new Segnalazione();
	            
	            segnalazione.setDescrizione(resultSet.getString("descrizione"));
	            segnalazione.setFoto(resultSet.getString("foto")); // Se necessario, gestire il recupero del BLOB
	            segnalazione.setStato(resultSet.getString("stato"));
	            segnalazione.setLatitudine(resultSet.getDouble("latitudine"));
	            segnalazione.setLongitudine(resultSet.getDouble("longitudine"));

	            // Aggiungi i punti assegnati all'oggetto Segnalazione
	            int puntiAssegnati = resultSet.getInt("punti"); // Questo potrebbe essere null, quindi gestire con un controllo

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

	            //if (connessione != null) connessione.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return segnalazioni;
	}

	
	@Override
	public List<Segnalazione> getSegnalazioni() throws SQLException {
	    List<Segnalazione> segnalazioni = new ArrayList<>();
	    Connection connessione = null;
	    PreparedStatement stmt = null;
	    ResultSet resultSet = null;

	    try {
	        // Ottieni la connessione al database
	        connessione = DBConnection.getConnection();

	        // Query SQL per ottenere tutte le segnalazioni
	        String sql = "SELECT id_segnalazione, id_utente, descrizione, foto, stato, latitudine, longitudine " +
	                     "FROM segnalazioni";

	        stmt = connessione.prepareStatement(sql);

	        // Esegui la query
	        resultSet = stmt.executeQuery();

	        // Itera attraverso i risultati e crea gli oggetti Segnalazione
	        while (resultSet.next()) {
	            Segnalazione segnalazione = new Segnalazione();

	            segnalazione.setIdSegnalazione(resultSet.getInt("id_segnalazione"));
	            segnalazione.setIdUtente(resultSet.getInt("id_utente"));
	            segnalazione.setDescrizione(resultSet.getString("descrizione"));
	            segnalazione.setFoto(resultSet.getString("foto")); // Gestisci il recupero del BLOB come necessario
	            segnalazione.setStato(resultSet.getString("stato"));
	            segnalazione.setLatitudine(resultSet.getDouble("latitudine"));
	            segnalazione.setLongitudine(resultSet.getDouble("longitudine"));

	            // Aggiungi la segnalazione alla lista
	            segnalazioni.add(segnalazione);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace(); // Gestione degli errori
	    } finally {
	        try {
	            if (resultSet != null) resultSet.close();
	            if (stmt != null) stmt.close();
	            // if (connessione != null) connessione.close(); // Non chiudere la connessione se gestita altrove
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return segnalazioni;
	}
	
	
	@Override
	public List<Segnalazione> getSegnalazioniByStato(String stato) throws SQLException {
	    List<Segnalazione> segnalazioni = new ArrayList<>();
	    Connection connessione = null;
	    PreparedStatement stmt = null;
	    ResultSet resultSet = null;

	    try {
	        // Ottieni la connessione al database
	        connessione = DBConnection.getConnection();

	        // Query SQL per ottenere le segnalazioni con un determinato stato
	        String sql = "SELECT id_segnalazione, id_utente, descrizione, foto, stato, latitudine, longitudine " +
	                     "FROM segnalazioni " +
	                     "WHERE stato = ?";

	        stmt = connessione.prepareStatement(sql);
	        stmt.setString(1, stato); // Imposta lo stato come parametro nella query

	        // Esegui la query
	        resultSet = stmt.executeQuery();

	        // Itera attraverso i risultati e crea gli oggetti Segnalazione
	        while (resultSet.next()) {
	            Segnalazione segnalazione = new Segnalazione();

	            segnalazione.setIdSegnalazione(resultSet.getInt("id_segnalazione"));
	            segnalazione.setIdUtente(resultSet.getInt("id_utente"));
	            segnalazione.setDescrizione(resultSet.getString("descrizione"));
	            segnalazione.setFoto(resultSet.getString("foto")); // Gestisci il recupero del BLOB come necessario
	            segnalazione.setStato(resultSet.getString("stato"));
	            segnalazione.setLatitudine(resultSet.getDouble("latitudine"));
	            segnalazione.setLongitudine(resultSet.getDouble("longitudine"));

	            // Aggiungi la segnalazione alla lista
	            segnalazioni.add(segnalazione);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace(); // Gestione degli errori
	    } finally {
	        try {
	            if (resultSet != null) resultSet.close();
	            if (stmt != null) stmt.close();
	            //if (connessione != null) connessione.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return segnalazioni;
	}


	
	@Override
	public void eliminaSegnalazione(int idSegnalazione) throws SQLException {
	    Connection connessione = null;
	    PreparedStatement stmt = null;

	    try {
	        // Ottieni la connessione al database
	        connessione = DBConnection.getConnection();
	        
	        // Query SQL per eliminare la segnalazione tramite il suo ID
	        String sql = "DELETE FROM segnalazioni WHERE id_segnalazione = ?";
	        
	        stmt = connessione.prepareStatement(sql);
	        stmt.setInt(1, idSegnalazione); // Imposta l'ID come parametro

	        int righeEliminate = stmt.executeUpdate();
	        if (righeEliminate > 0) {
	            connessione.commit();
	            System.out.println("Segnalazione eliminata con successo");
	        } else {
	            connessione.rollback();
	            System.out.println("Errore: nessuna segnalazione trovata con l'ID specificato");
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw e; // Rilancia l'eccezione per la gestione a un livello superiore
	    } finally {
	        try {
	            if (stmt != null) stmt.close();
	            //if (connessione != null) connessione.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
	@Override
	public void assegnaOperatore(int idSegnalazione, int idOperatore, int idEsperto) {
	    Connection connessione = null;
	    PreparedStatement stmtAssegnazione = null;
	    PreparedStatement stmtAggiornaStato = null;
	    // faccio entrambi insieme nella stessa funzione perchè se fallisce uno dei due non può avere successo l'altro, ci sarebbero dati non coerenti

	    try {
	        // Ottieni la connessione al database
	        connessione = DBConnection.getConnection();
	        connessione.setAutoCommit(false); // Imposta la transazione manuale

	        // Query SQL per inserire una nuova assegnazione
	        String sqlAssegnazione = "INSERT INTO assegnazioni (id_segnalazione, id_operatore, id_esperto, assegnato_at) " +
	                                 "VALUES (?, ?, ?, NOW())";

	        stmtAssegnazione = connessione.prepareStatement(sqlAssegnazione);
	        stmtAssegnazione.setInt(1, idSegnalazione);
	        stmtAssegnazione.setInt(2, idOperatore);
	        stmtAssegnazione.setInt(3, idEsperto);

	        int righeInserite = stmtAssegnazione.executeUpdate();

	        if (righeInserite > 0) {
	            // Query SQL per aggiornare lo stato della segnalazione a 'In corso'
	            String sqlAggiornaStato = "UPDATE segnalazioni SET stato = 'In corso' WHERE id_segnalazione = ?";

	            stmtAggiornaStato = connessione.prepareStatement(sqlAggiornaStato);
	            stmtAggiornaStato.setInt(1, idSegnalazione);
	            stmtAggiornaStato.executeUpdate();

	            connessione.commit(); // Commit della transazione
	        } else {
	            connessione.rollback(); // Rollback della transazione se l'inserimento fallisce
	        }

	    } catch (SQLException e) {
	        e.printStackTrace(); // Gestione degli errori
	        if (connessione != null) {
	            try {
	                connessione.rollback(); // Rollback in caso di errore
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	    } finally {
	        try {
	            if (stmtAssegnazione != null) stmtAssegnazione.close();
	            if (stmtAggiornaStato != null) stmtAggiornaStato.close();
	            // if (connessione != null) connessione.close();
	        } catch (SQLException e) {
	            e.printStackTrace(); // Gestione degli errori di chiusura
	        }
	    }
	}


	@Override
	public void assegnaPunti(int idSegnalazione, int punti) {
	    Connection connessione = null;
	    PreparedStatement stmtPunti = null;
	    PreparedStatement stmtAggiornaStato = null;

	    try {
	        // Ottieni la connessione al database
	        connessione = DBConnection.getConnection();
	        connessione.setAutoCommit(false); // Imposta la transazione manuale

	        // Query SQL per inserire o aggiornare i punti per la segnalazione
	        String sqlPunti = "INSERT INTO punti_segnalazioni (id_segnalazione, punti) " +
	                          "VALUES (?, ?) " +
	                          "ON DUPLICATE KEY UPDATE punti = ?";

	        stmtPunti = connessione.prepareStatement(sqlPunti);
	        stmtPunti.setInt(1, idSegnalazione);  // Imposta l'ID della segnalazione come parametro
	        stmtPunti.setInt(2, punti);            // Imposta i punti da assegnare come parametro
	        stmtPunti.setInt(3, punti);            // Aggiorna i punti in caso di conflitto

	        int righeInserite = stmtPunti.executeUpdate();

	        if (righeInserite > 0) {
	            // Query SQL per aggiornare lo stato della segnalazione a 'Riscontrata'
	            String sqlAggiornaStato = "UPDATE segnalazioni SET stato = 'Riscontrata' WHERE id_segnalazione = ?";
	            stmtAggiornaStato = connessione.prepareStatement(sqlAggiornaStato);
	            stmtAggiornaStato.setInt(1, idSegnalazione);
	            stmtAggiornaStato.executeUpdate();

	            connessione.commit(); // Commit della transazione
	        } else {
	            connessione.rollback(); // Rollback della transazione se l'inserimento fallisce
	        }

	    } catch (SQLException e) {
	        e.printStackTrace(); // Gestione degli errori
	        if (connessione != null) {
	            try {
	                connessione.rollback(); // Rollback in caso di errore
	            } catch (SQLException rollbackEx) {
	                rollbackEx.printStackTrace(); // Gestione errore rollback
	            }
	        }
	    } finally {
	        try {
	            if (stmtPunti != null) stmtPunti.close(); // Chiudi il PreparedStatement per i punti
	            if (stmtAggiornaStato != null) stmtAggiornaStato.close(); // Chiudi il PreparedStatement per l'aggiornamento dello stato
	            // if (connessione != null) connessione.close();
	        } catch (SQLException e) {
	            e.printStackTrace(); // Gestione degli errori di chiusura
	        }
	    }
	}


	
	
	@Override
	public List<Segnalazione> getSegnalazioniAssegnate(int idOperatore, String stato) throws SQLException {
	    List<Segnalazione> segnalazioni = new ArrayList<>();
	    Connection connessione = null;
	    PreparedStatement stmt = null;
	    ResultSet resultSet = null;

	    try {
	        // Ottieni la connessione al database
	        connessione = DBConnection.getConnection();

	        // Query SQL per ottenere le segnalazioni assegnate a un operatore specifico con stato "In corso"
	        String sql = "SELECT s.id_segnalazione, s.id_utente, s.descrizione, s.foto, s.stato, s.latitudine, s.longitudine " +
	                     "FROM segnalazioni s " +
	                     "JOIN assegnazioni a ON s.id_segnalazione = a.id_segnalazione " +
	                     "WHERE a.id_operatore = ? AND s.stato = ?";

	        stmt = connessione.prepareStatement(sql);
	        stmt.setInt(1, idOperatore); // Imposta l'ID dell'operatore come parametro
	        stmt.setString(2, stato);     // Imposta lo stato come parametro nella query

	        // Esegui la query
	        resultSet = stmt.executeQuery();

	        // Itera attraverso i risultati e crea gli oggetti Segnalazione
	        while (resultSet.next()) {
	            Segnalazione segnalazione = new Segnalazione();

	            segnalazione.setIdSegnalazione(resultSet.getInt("id_segnalazione"));
	            segnalazione.setIdUtente(resultSet.getInt("id_utente"));
	            segnalazione.setDescrizione(resultSet.getString("descrizione"));
	            segnalazione.setFoto(resultSet.getString("foto")); 
	            segnalazione.setStato(resultSet.getString("stato"));
	            segnalazione.setLatitudine(resultSet.getDouble("latitudine"));
	            segnalazione.setLongitudine(resultSet.getDouble("longitudine"));

	            // Aggiungi la segnalazione alla lista
	            segnalazioni.add(segnalazione);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace(); // Gestione degli errori
	    } finally {
	        try {
	            if (resultSet != null) resultSet.close();
	            if (stmt != null) stmt.close();
	            // if (connessione != null) connessione.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return segnalazioni;
	}

	
	
	
	
	
	
	
	
	
	
	
	@Override
	public void aggiornaStato(int idSegnalazione) {
	    Connection connessione = null;
	    PreparedStatement stmt = null;

	    try {
	        // Ottieni la connessione al database
	        connessione = DBConnection.getConnection();

	        // Query SQL per aggiornare lo stato della segnalazione
	        String sql = "UPDATE segnalazioni SET stato = ? WHERE id_segnalazione = ?";

	        stmt = connessione.prepareStatement(sql);
	        stmt.setString(1, "Risolta"); // Imposta lo stato a "Risolta"
	        stmt.setInt(2, idSegnalazione); // Imposta l'ID della segnalazione come parametro

	        int righeAggiornate = stmt.executeUpdate();
	        if (righeAggiornate > 0) {
	            connessione.commit(); // Commit della transazione
	        } else {
	            connessione.rollback(); // Rollback della transazione se nessuna riga è stata aggiornata
	        }

	    } catch (SQLException e) {
	        e.printStackTrace(); // Gestione degli errori
	        if (connessione != null) {
	            try {
	                connessione.rollback(); // Rollback in caso di errore
	            } catch (SQLException rollbackEx) {
	                rollbackEx.printStackTrace(); // Gestione errore rollback
	            }
	        }
	    } finally {
	        try {
	            if (stmt != null) stmt.close(); // Chiudi il PreparedStatement
	            // Se necessario, chiudi anche la connessione qui
	            // if (connessione != null) connessione.close();
	        } catch (SQLException e) {
	            e.printStackTrace(); // Gestione degli errori di chiusura
	        }
	    }
	}

	
	
}

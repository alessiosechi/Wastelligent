package logic.model.dao.queries;



import logic.model.domain.Segnalazione;
import logic.model.domain.StatoSegnalazione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SegnalazioneQueries {
	private SegnalazioneQueries() {}
	public static int inserisciSegnalazione(Connection conn, Segnalazione segnalazione) throws SQLException {
	    String insertStatement = "INSERT INTO segnalazioni (id_utente, descrizione, foto, stato, latitudine, longitudine) " +
	                             "VALUES (?, ?, ?, ?, ?, ?)";

	    try (PreparedStatement stmt = conn.prepareStatement(insertStatement)) {
	        stmt.setInt(1, segnalazione.getIdUtente());
	        stmt.setString(2, segnalazione.getDescrizione()); 
            stmt.setString(3, segnalazione.getFoto()); 
	        stmt.setString(4, segnalazione.getStato()); 
	        stmt.setDouble(5, segnalazione.getLatitudine()); 
	        stmt.setDouble(6, segnalazione.getLongitudine());

	        return stmt.executeUpdate();
	    }
	}

    
	public static int eliminaSegnalazione(Connection conn, Segnalazione segnalazione) throws SQLException {
	    String deleteStatement = "DELETE FROM segnalazioni WHERE id_segnalazione = ?";

	    try (PreparedStatement stmt = conn.prepareStatement(deleteStatement)) {
	        stmt.setInt(1, segnalazione.getIdSegnalazione()); 

	        return stmt.executeUpdate();
	    }
	}


	public static int aggiornaStato(Connection conn, int idSegnalazione, String stato) throws SQLException {
	    String updateStatement = "UPDATE segnalazioni SET stato = ? WHERE id_segnalazione = ?";

	    try (PreparedStatement stmt = conn.prepareStatement(updateStatement)) {
	        stmt.setString(1, stato);               
	        stmt.setInt(2, idSegnalazione);        

	        return stmt.executeUpdate();
	    }
	}

    
    public static ResultSet getSegnalazioniByStato(Connection conn, String stato) throws SQLException {
        String query = "SELECT id_segnalazione, id_utente, descrizione, foto, stato, latitudine, longitudine " +
                       "FROM segnalazioni WHERE stato = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, stato); 

            return stmt.executeQuery();
        }
    }
   

    
    public static ResultSet getSegnalazioniAssegnate(Connection conn, int idOperatore) throws SQLException {
        String query = "SELECT s.id_segnalazione, s.id_utente, s.descrizione, s.foto, s.stato, s.latitudine, s.longitudine " +
                       "FROM assegnazioni a " +
                       "JOIN segnalazioni s ON a.id_segnalazione = s.id_segnalazione " +
                       "WHERE a.id_operatore = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idOperatore); 
            return stmt.executeQuery();
        }

    }


    
    
    
    public static ResultSet getSegnalazioniRiscontrate(Connection conn, int idUtente) throws SQLException {
        String query = "SELECT id_segnalazione, id_utente, descrizione, foto, stato, latitudine, longitudine, punti_assegnati " +
                       "FROM segnalazioni " +
                       "WHERE id_utente = ? AND stato = ?";


        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, idUtente);                                 
            stmt.setString(2, StatoSegnalazione.RISCONTRATA.getStato()); 

            return stmt.executeQuery();
        }

    }

    
    
    
    public static int inserisciAssegnazione(Connection conn, int idSegnalazione, int idOperatore) throws SQLException {
        String insertStatement = "INSERT INTO assegnazioni (id_segnalazione, id_operatore) VALUES (?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(insertStatement)) {
            stmt.setInt(1, idSegnalazione); 
            stmt.setInt(2, idOperatore);     

            return stmt.executeUpdate();
        }
        

    }

    
    
    public static int assegnaPuntiSegnalazione(Connection conn, int idSegnalazione, int punti) throws SQLException {
        String updateStatement = "UPDATE segnalazioni SET punti_assegnati = ? WHERE id_segnalazione = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateStatement)) {
            stmt.setInt(1, punti);           
            stmt.setInt(2, idSegnalazione);  

            return stmt.executeUpdate();
        }
    }


}

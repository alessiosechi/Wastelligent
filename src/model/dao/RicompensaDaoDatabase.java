package model.dao;

import model.domain.Ricompensa;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class RicompensaDaoDatabase implements RicompensaDao {
	
	
	
	private RicompensaDaoDatabase(){
	}

	private static volatile RicompensaDaoDatabase instance;

	public static RicompensaDaoDatabase getInstance() {
		RicompensaDaoDatabase result = instance;

		if (instance == null) {
			// blocco sincronizzato
			synchronized (RicompensaDaoDatabase.class) {
				result = instance;
				if (result == null) {
					instance = result = new RicompensaDaoDatabase();
				}

			}
		}

		return result;
	}

	@Override
	public void registraRicompensaRiscattata(Ricompensa ricompensa) {
		Connection connessione = null;
		PreparedStatement stmt = null;

		try {
			// Ottieni la connessione al database
			connessione = DBConnection.getConnection();
			String sql = "INSERT INTO ricompense (id_utente, nome, descrizione, codice_riscatto, valore, data_riscatto, data_scadenza, punti_utilizzati) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
			stmt = connessione.prepareStatement(sql);

			// Imposta i parametri della query
			stmt.setInt(1, ricompensa.getIdUtente());
			stmt.setString(2, ricompensa.getNome());
			stmt.setString(3, ricompensa.getDescrizione());
			stmt.setString(4, ricompensa.getCodiceRiscatto()); 
			stmt.setBigDecimal(5, BigDecimal.valueOf(ricompensa.getValore()));
			stmt.setDate(6, java.sql.Date.valueOf(ricompensa.getDataRiscatto()));

			stmt.setDate(7, java.sql.Date.valueOf(ricompensa.getDataScadenza()));

			stmt.setInt(8, ricompensa.getPunti());

			int righeInserite = stmt.executeUpdate();
			if (righeInserite > 0) {
				connessione.commit();
			} else {
				connessione.rollback();
			}
		} catch (SQLException e) {

			e.printStackTrace(); 
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	
	
	
	
	
	
	
	
	
	@Override
	public List<Ricompensa> getRicompenseByUtente(int idUtente) {
	    List<Ricompensa> ricompense = new ArrayList<>();
	    Connection connessione = null;
	    PreparedStatement stmt = null;
	    ResultSet resultSet = null;
	    
	    


	    try {
	        // Ottieni la connessione al database
	        connessione = DBConnection.getConnection();

	        // Query SQL per ottenere le ricompense di uno specifico utente
	        String sql = "SELECT id_ricompensa, nome, descrizione, codice_riscatto, valore, data_riscatto, data_scadenza, punti_utilizzati " +
	                     "FROM ricompense WHERE id_utente = ?";

	        stmt = connessione.prepareStatement(sql);
	        stmt.setInt(1, idUtente);

	        // Esegui la query
	        resultSet = stmt.executeQuery();

	        // Itera attraverso i risultati e crea gli oggetti Ricompensa
	        while (resultSet.next()) {
	        	
	        	
	            String nome = resultSet.getString("nome");
	            String descrizione = resultSet.getString("descrizione");
	            String codiceRiscatto = resultSet.getString("codice_riscatto");
	            int valore = resultSet.getInt("valore");
	            Date dataRiscatto = resultSet.getDate("data_riscatto");
	            Date dataScadenza = resultSet.getDate("data_scadenza");
	            int puntiUtilizzati = resultSet.getInt("punti_utilizzati");

	            Ricompensa ricompensa = new Ricompensa(nome,descrizione, valore,codiceRiscatto, dataRiscatto.toString(), dataScadenza.toString(), puntiUtilizzati);
	            ricompense.add(ricompensa);
	            
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

	    return ricompense;
	}

}

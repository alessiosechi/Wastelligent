package logic.model.dao.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtenteBaseQueries {
	private UtenteBaseQueries() {
	}

	public static ResultSet estraiPunti(Connection connessione, int idUtente) throws SQLException {
		String sql = "SELECT punti FROM punti_utenti WHERE id_utente = ?";

		try (PreparedStatement stmt = connessione.prepareStatement(sql)) {
			stmt.setInt(1, idUtente);

			return stmt.executeQuery();
		}
	}

	public static int aggiungiPunti(Connection connessione, int idUtente, int puntiDaAggiungere) throws SQLException {
	    String sql = "INSERT INTO punti_utenti (id_utente, punti) VALUES (?, ?) ON DUPLICATE KEY UPDATE punti = punti + VALUES(punti)";

	    try (PreparedStatement stmt = connessione.prepareStatement(sql)) {
	        stmt.setInt(1, idUtente);
	        stmt.setInt(2, puntiDaAggiungere);
	        return stmt.executeUpdate();
	    }
	}

	public static int sottraiPunti(Connection connessione, int idUtente, int puntiDaSottrarre) throws SQLException {
		String sql = "UPDATE punti_utenti SET punti = punti - ? WHERE id_utente = ?";

		try (PreparedStatement stmt = connessione.prepareStatement(sql)) {
			stmt.setInt(1, puntiDaSottrarre);
			stmt.setInt(2, idUtente);

			return stmt.executeUpdate();
		}
	}
}

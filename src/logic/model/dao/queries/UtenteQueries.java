package logic.model.dao.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import logic.model.domain.Ruolo;

public class UtenteQueries {
	private UtenteQueries() {
	}

	public static ResultSet login(Connection conn, String username, String password) throws SQLException {
		String sql = "SELECT username FROM utenti WHERE username = ? AND password_hash = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			return stmt.executeQuery();
		}
	}

	public static ResultSet getRuoloIdByUsername(Connection conn, String username) throws SQLException {
		String sql = "SELECT r.id_ruolo FROM utenti u JOIN ruoli r ON u.tipo_utente = r.id_ruolo WHERE u.username = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username);
			return stmt.executeQuery();
		}
	}

	public static ResultSet getIdByUsername(Connection conn, String username) throws SQLException {
		String sql = "SELECT id_utente FROM utenti WHERE username = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username);
			return stmt.executeQuery();
		}
	}

	public static int registrazione(Connection conn, String username, String password, Ruolo ruolo)
			throws SQLException {
		String sql = "INSERT INTO utenti (username, password_hash, tipo_utente) VALUES (?, ?, ?)";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username);
			stmt.setString(2, password);
			stmt.setInt(3, ruolo.getId());
			return stmt.executeUpdate();
		}
	}

	public static ResultSet isUsernameTaken(Connection conn, String username) throws SQLException {
		String sql = "SELECT COUNT(*) AS username_count FROM utenti WHERE username = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username);
			return stmt.executeQuery();
		}

	}

	public static ResultSet getUtenti(Connection conn, Ruolo ruolo) throws SQLException {
		String sql = "SELECT id_utente, username, tipo_utente FROM utenti WHERE tipo_utente = ?";
		try (PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, ruolo.getId());
			return stmt.executeQuery();
		}

	}
}

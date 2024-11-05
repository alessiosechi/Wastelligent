package model.dao.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.dao.DBConnection;

public class LoginQueries {
	
	
	private LoginQueries() {
		// costruttore vuoto
	}

    public static ResultSet login(String username, String password) throws SQLException {
        String sql = "SELECT r.nome FROM utenti u JOIN ruoli r ON u.tipo_utente = r.id_ruolo WHERE u.username = ? AND u.password_hash = ?";
        
        Connection connessione = DBConnection.getConnection();
        PreparedStatement stmt = connessione.prepareStatement(sql);
        
        stmt.setString(1, username);
        stmt.setString(2, password);

        return stmt.executeQuery();  
    }
	
	
	
}

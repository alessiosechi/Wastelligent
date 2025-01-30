package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import logic.model.dao.DBConnection;

import java.sql.Connection;

public class TestDBConnection {

	@Test
	public void testConnessioneStabilitaConSuccesso() {
		// verifico se la connessione viene stabilita
		Connection conn = DBConnection.getConnection();
		assertNotNull(conn);
	}
}

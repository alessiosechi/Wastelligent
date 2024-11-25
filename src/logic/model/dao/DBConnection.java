package logic.model.dao;

import java.io.*;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class DBConnection { // potrei dichiararla protected
	private static Connection connection;
	private static final Logger logger = Logger.getLogger(DBConnection.class.getName());

	private DBConnection() {
	}

	static {
		// codice che viene eseguito dalla JVM la prima volta
		try (InputStream input = new FileInputStream("resources/db.properties")) {
			Properties properties = new Properties();
			properties.load(input);

			String connectionUrl = properties.getProperty("CONNECTION_URL");
			String user = properties.getProperty("LOGIN_USER");
			String pass = properties.getProperty("LOGIN_PASS");

			connection = DriverManager.getConnection(connectionUrl, user, pass);
			

		} catch (IOException | SQLException e) {
			logger.severe("Si è verificato un errore: " + e.getMessage());
		}
	}

	public static Connection getConnection() {
		return connection;
	}
	
	
	

	/*
	 * dalle slide: "SE POSSIBILE APRIRE UNA SOLA CONNESSIONE PER APPLICAZIONE PER
	 * CHIUDERLA SOLO QUANDO NON CI SARANNO ULTERIORI INTERAZIONI CON IL DBMS"
	 */
}

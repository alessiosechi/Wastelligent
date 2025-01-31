package logic.model.dao;

import java.io.*;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class DBConnection {
	private static Connection connection;
	private static final Logger logger = Logger.getLogger(DBConnection.class.getName());

	private DBConnection() {
	}

	static {
		// codice che viene eseguito dalla JVM una sola volta (quando la classe viene caricata in memoria dalla JVM)
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

	// apro una sola connessione che verrà chiusa quando non ci saranno ulteriori
	// interazioni con il DBMS (al termine dell'applicazione)

}

package model.dao;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class DBConnection {
	private static Connection connection;

	private DBConnection() {
	}

	static {
		// codice che viene eseguito dalla JVM la prima volta
		// Does not work if generating a jar file
		try (InputStream input = new FileInputStream("resources/db.properties")) {
			Properties properties = new Properties();
			properties.load(input);

			String connectionUrl = properties.getProperty("CONNECTION_URL");
			String user = properties.getProperty("LOGIN_USER");
			String pass = properties.getProperty("LOGIN_PASS");

			connection = DriverManager.getConnection(connectionUrl, user, pass);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() throws SQLException {
		return connection;
	}

}

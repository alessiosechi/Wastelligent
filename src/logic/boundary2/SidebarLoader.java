package logic.boundary2;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.logging.Logger;

public class SidebarLoader {
	private static final Logger logger = Logger.getLogger(SidebarLoader.class.getName());

	private SidebarLoader() {
	}

	public static AnchorPane caricaSidebar(SidebarType type) {
		try {
			FXMLLoader loader = new FXMLLoader(SidebarLoader.class.getResource("Sidebar.fxml"));

			loader.setController(new SidebarController(type));

			return loader.load();
		} catch (IOException e) {
			logger.severe("Errore nel caricamento del file Sidebar.fxml: " + e.getMessage());
			return null;
		}
	}

}

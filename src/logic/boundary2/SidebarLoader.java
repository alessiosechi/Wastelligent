package logic.boundary2;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SidebarLoader {

	public static AnchorPane loadSidebar(SidebarType type) {
		try {
			FXMLLoader loader = new FXMLLoader(SidebarLoader.class.getResource("Sidebar.fxml"));

			
            loader.setController(new SidebarController(type));
			
			return loader.load();
		} catch (IOException e) {
			throw new RuntimeException("Unable to load Sidebar.fxml", e);
		}
	}

}

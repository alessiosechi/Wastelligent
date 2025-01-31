package logic.boundary2;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import logic.boundary.components.ViewInfo;
import logic.boundary.components.ViewLoader;

public class SidebarController {

	@FXML
	private Button sidebarButton1;

	@FXML
	private Button sidebarButton2;

	@FXML
	private Button exitButton;

	private SidebarType type;

	public SidebarController(SidebarType type) {
		this.type = type;
	}

	@FXML
	public void initialize() {
		exitButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.LOGIN_VIEW));

		aggiungiEffettoHover(sidebarButton1);
		aggiungiEffettoHover(sidebarButton2);

		configuraSidebar();
	}

	private void aggiungiEffettoHover(Button button) {
		String coloreOriginale = "#216838";
		String coloreHover = "#1A5A31";

		button.setOnMouseEntered(event -> {
			button.setStyle("-fx-background-color: " + coloreHover + ";");
		});

		button.setOnMouseExited(event -> {
			button.setStyle("-fx-background-color: " + coloreOriginale + ";");
		});

	}

	private void configuraSidebar() {

		switch (type) {
		case UTENTE_BASE_SIDEBAR:
			sidebarButton1.setText("SEGNALA\nRIFIUTI");
			sidebarButton1.setOnAction(event -> ViewLoader.caricaView(ViewInfo.SEGNALA_RIFIUTI_VIEW));

			sidebarButton2.setText("RISCATTA\nRICOMPENSA");
			sidebarButton2.setOnAction(event -> ViewLoader.caricaView(ViewInfo.EFFETTUA_RISCATTO_VIEW));
			break;

		case ESPERTO_ECOLOGICO_SIDEBAR:
			sidebarButton1.setText("ASSEGNA\nSEGNALAZIONI");
			sidebarButton1.setOnAction(event -> ViewLoader.caricaView(ViewInfo.ASSEGNA_SEGNALAZIONI_VIEW));
			sidebarButton2.setText("VALUTA\nSEGNALAZIONI");
			sidebarButton2.setOnAction(event -> ViewLoader.caricaView(ViewInfo.VALUTA_SEGNALAZIONI_VIEW));

			break;

		case OPERATORE_ECOLOGICO_SIDEBAR:
			sidebarButton1.setText("SEGNALAZIONI\nASSEGNATE");
			sidebarButton1.setOnAction(event -> ViewLoader.caricaView(ViewInfo.ASSEGNAZIONI_OPERATORE_VIEW));
			sidebarButton1.setLayoutY(339);

			sidebarButton2.setDisable(true);
			sidebarButton2.setVisible(false);

			break;
		}
	}

}

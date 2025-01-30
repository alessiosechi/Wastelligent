package logic.boundary2;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import logic.boundary.ViewInfo;
import logic.boundary.ViewLoader;

public class SidebarController {
	@FXML
	private AnchorPane sidebarPane;
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
        configureSidebar();
	}
    private void configureSidebar() {

        switch (type) {
            case UTENTE_BASE_SIDEBAR:
        		sidebarButton1.setText("SEGNALA\nRIFIUTI");
        		sidebarButton1.setOnAction(event -> ViewLoader.caricaView(ViewInfo.SEGNALA_RIFIUTI_VIEW));

        		sidebarButton2.setText("RISCATTA\nRICOMPENSA");
        		sidebarButton2.setOnAction(event -> ViewLoader.caricaView(ViewInfo.EFFETTUA_RISCATTO_VIEW));
                break;

            case ESPERTO_ECOLOGICO_SIDEBAR:
                sidebarButton1.setText("Analizza Segnalazioni");
                sidebarButton2.setText("Gestisci Statistiche");

                break;

            case OPERATORE_ECOLOGICO_SIDEBAR:
                sidebarButton1.setText("Gestisci Utenti");

                sidebarButton2.setText("Visualizza Report");

                break;
        }
    }
	

}

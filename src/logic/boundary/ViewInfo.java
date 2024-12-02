package logic.boundary;

public enum ViewInfo {
	CONFIGURATION_VIEW("ConfigurationView.fxml", "Configurazione"),
    LOGIN_VIEW("LoginView.fxml", "Login"),
    REGISTRAZIONE_VIEW("RegistrazioneView.fxml", "Registrazione"),
    EFFETTUA_SEGNALAZIONE_VIEW("EffettuaSegnalazioneView.fxml", "Effettua Segnalazione"),
    RISCATTA_RICOMPENSA_VIEW("RiscattaRicompensaView.fxml", "Riscatta Ricompensa"),
    STORICO_VIEW("StoricoView.fxml", "Storico Segnalazioni"),
    GESTISCI_SEGNALAZIONI_VIEW("GestisciSegnalazioniView.fxml", "Gestisci Segnalazioni"),
    ASSEGNA_PUNTI_VIEW("AssegnaPuntiView.fxml", "Assegna Punti"),
    SEGNALAZIONI_ASSEGNATE_VIEW("SegnalazioniAssegnateView.fxml", "Segnalazioni Assegnate"),
    DETTAGLI_VIEW("DettagliSegnalazioneView.fxml", "Dettagli Segnalazione");
	

    private final String fxmlPath;
    private final String title;

    // costruttore dell'enum
    ViewInfo(String fxmlPath, String title) {
        this.fxmlPath = fxmlPath;
        this.title = title;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }

    public String getTitle() {
        return title;
    }
    
    // metodo per ottenere il ViewInfo in base al percorso FXML
    public static ViewInfo fromFxmlPath(String fxmlPath) {
        for (ViewInfo viewInfo : values()) {
            if (viewInfo.getFxmlPath().equalsIgnoreCase(fxmlPath)) {
                return viewInfo;
            }
        }
        throw new IllegalArgumentException("No ViewInfo found for FXML path: " + fxmlPath);
    }
}


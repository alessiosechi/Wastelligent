package logic.boundary.components;

public enum ViewInfo {
	CONFIGURATION_VIEW("/logic/boundary/components/ConfigurationView.fxml", "Configurazione"),
    LOGIN_VIEW("/logic/boundary/components/LoginView.fxml", "Login"),
    REGISTRAZIONE_VIEW("/logic/boundary/components/RegistrazioneView.fxml", "Registrazione"),
    EFFETTUA_SEGNALAZIONE_VIEW("/logic/boundary/EffettuaSegnalazioneView.fxml", "Effettua Segnalazione"),
    SEGNALA_RIFIUTI_VIEW("/logic/boundary2/SegnalaRifiutiView.fxml", "Effettua Segnalazione"),  
    RISCATTA_RICOMPENSA_VIEW("/logic/boundary/RiscattaRicompensaView.fxml", "Riscatta Ricompensa"),
    EFFETTUA_RISCATTO_VIEW("/logic/boundary2/EffettuaRiscattoView.fxml", "Effettua Riscatto"),
    STORICO_VIEW("/logic/boundary/StoricoView.fxml", "Storico Segnalazioni"),
    STORICO_PUNTI_VIEW("/logic/boundary2/StoricoPuntiView.fxml", "Storico Punti"),
    GESTISCI_SEGNALAZIONI_VIEW("/logic/boundary/GestisciSegnalazioniView.fxml", "Gestisci Segnalazioni"),
    ASSEGNA_SEGNALAZIONI_VIEW("/logic/boundary2/AssegnaSegnalazioniView.fxml", "Assegna Segnalazioni"),
    ASSEGNA_PUNTI_VIEW("/logic/boundary/AssegnaPuntiView.fxml", "Assegna Punti"),
    VALUTA_SEGNALAZIONI_VIEW("/logic/boundary2/ValutaSegnalazioniView.fxml", "Valuta Segnalazioni"),
    SEGNALAZIONI_ASSEGNATE_VIEW("/logic/boundary/SegnalazioniAssegnateView.fxml", "Segnalazioni Assegnate"),
    ASSEGNAZIONI_OPERATORE_VIEW("/logic/boundary2/AssegnazioniOperatoreView.fxml", "Visualizza assegnazioni"),
    DETTAGLI_VIEW("/logic/boundary/DettagliSegnalazioneView.fxml", "Dettagli Segnalazione");
	

    private final String fxmlPath;
    private final String title;

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


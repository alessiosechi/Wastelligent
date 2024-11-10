package boundary;

public enum ViewInfo {
    LOGIN_VIEW("LoginView.fxml", "Login", LoginViewController.class),
    REGISTRAZIONE_VIEW("RegistrazioneView.fxml", "Registrazione", RegistrazioneViewController.class),
    EFFETTUA_SEGNALAZIONE_VIEW("EffettuaSegnalazioneView.fxml", "Effettua Segnalazione", EffettuaSegnalazioneViewController.class),
    RISCATTA_RICOMPENSA_VIEW("RiscattaRicompensaView.fxml", "Riscatta Ricompensa", RiscattaRicompensaViewController.class),
    STORICO_VIEW("StoricoView.fxml", "Storico Segnalazioni", StoricoViewController.class),
    GESTISCI_SEGNALAZIONI_VIEW("GestisciSegnalazioniView.fxml", "Gestisci Segnalazioni", GestisciSegnalazioniViewController.class),
    ASSEGNA_PUNTI_VIEW("AssegnaPuntiView.fxml", "Assegna Punti", AssegnaPuntiViewController.class),
    SEGNALAZIONI_ASSEGNATE_VIEW("SegnalazioniAssegnateView.fxml", "Segnalazioni Assegnate", SegnalazioniAssegnateViewController.class),
    DETTAGLI_VIEW("DettagliSegnalazioneView.fxml", "Dettagli Segnalazione", DettagliSegnalazioneViewController.class);
	

    private final String fxmlPath;
    private final String title;
    private final Class<?> controllerClass;

    // costruttore dell'enum
    ViewInfo(String fxmlPath, String title, Class<?> controllerClass) {
        this.fxmlPath = fxmlPath;
        this.title = title;
        this.controllerClass = controllerClass;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }

    public String getTitle() {
        return title;
    }
    
    public Class<?> getControllerClass() {
        return controllerClass;
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


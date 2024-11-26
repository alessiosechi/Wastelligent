package logic.boundary;

public class ControllerFactory {
    
    public static Object getController(ViewInfo viewInfo) {
        switch (viewInfo) {
            case LOGIN_VIEW:
                return LoginViewController.getInstance();
            case REGISTRAZIONE_VIEW:
                return RegistrazioneViewController.getInstance();
            case EFFETTUA_SEGNALAZIONE_VIEW:
                return EffettuaSegnalazioneViewController.getInstance();
            case RISCATTA_RICOMPENSA_VIEW:
                return RiscattaRicompensaViewController.getInstance();
            case STORICO_VIEW:
                return StoricoViewController.getInstance();
            case GESTISCI_SEGNALAZIONI_VIEW:
                return GestisciSegnalazioniViewController.getInstance();
            case ASSEGNA_PUNTI_VIEW:
                return AssegnaPuntiViewController.getInstance();
            case SEGNALAZIONI_ASSEGNATE_VIEW:
                return SegnalazioniAssegnateViewController.getInstance();
            case DETTAGLI_VIEW:
                return DettagliSegnalazioneViewController.getInstance();
            default:
                return null;
        }
    }
}

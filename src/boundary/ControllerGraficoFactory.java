package boundary;


public class ControllerGraficoFactory {

    public Object createController(ViewInfo viewInfo) {
        switch (viewInfo) {
            case LOGIN_VIEW:
                return LoginViewController.getInstance();
            case EFFETTUA_SEGNALAZIONE_VIEW:
                return EffettuaSegnalazioneViewController.getInstance();
            default:
                throw new IllegalArgumentException("View non trovata per il tipo: " + viewInfo);
        }
    }
}


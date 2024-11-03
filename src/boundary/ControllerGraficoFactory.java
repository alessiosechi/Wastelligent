package boundary;

import java.util.HashMap;
import java.util.Map;

public class ControllerGraficoFactory {
    
    private final Map<String, Object> controllerMap; // Mappa controller
    private final Map<String, String> titoloMap;     // Mappa titoli

    public ControllerGraficoFactory() {

        controllerMap = new HashMap<>();
        titoloMap = new HashMap<>();

        // Associazioni FXML con i relativi controller grafici
        controllerMap.put("LoginView.fxml", LoginViewController.getInstance());
        controllerMap.put("RegistrazioneView.fxml", RegistrazioneViewController.getInstance());
        controllerMap.put("EffettuaSegnalazioneView.fxml", EffettuaSegnalazioneViewController.getInstance());
        controllerMap.put("RiscattaRicompensaView.fxml", RiscattaRicompensaViewController.getInstance());
        controllerMap.put("StoricoView.fxml", StoricoViewController.getInstance());
        controllerMap.put("DettagliSegnalazioneView.fxml", DettagliSegnalazioneViewController.getInstance());
        controllerMap.put("GestisciSegnalazioniView.fxml", GestisciSegnalazioniViewController.getInstance());
        controllerMap.put("AssegnaPuntiView.fxml", AssegnaPuntiViewController.getInstance());
        controllerMap.put("SegnalazioniAssegnateView.fxml", SegnalazioniAssegnateViewController.getInstance());

        // Associazioni FXML con i relativi titoli
        titoloMap.put("LoginView.fxml", "Login");
        titoloMap.put("RegistrazioneView.fxml", "Registrazione");
        titoloMap.put("EffettuaSegnalazioneView.fxml", "Effettua Segnalazione");
        titoloMap.put("RiscattaRicompensaView.fxml", "Riscatta Ricompensa");
        titoloMap.put("StoricoView.fxml", "Storico Segnalazioni");
        titoloMap.put("DettagliSegnalazioneView.fxml", "Dettagli segnalazione");
        titoloMap.put("GestisciSegnalazioniView.fxml", "Gestisci Segnalazioni");
        titoloMap.put("AssegnaPuntiView.fxml", "Assegna Punti");
        titoloMap.put("SegnalazioniAssegnateView.fxml", "Segnalazioni Assegnate");
    }

    public Object getController(String percorsoFXML) {
        return controllerMap.get(percorsoFXML);
    }

    public String getTitolo(String percorsoFXML) {
        return titoloMap.get(percorsoFXML);
    }
}


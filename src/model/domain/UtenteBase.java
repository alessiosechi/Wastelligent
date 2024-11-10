package model.domain;

public class UtenteBase extends Utente{
    public UtenteBase(int idUtente, String username) {
        super(idUtente, username);
    }

    @Override
    public String getViewIniziale(int interfaccia) {
        if (interfaccia == 1) {
            return "EffettuaSegnalazioneView.fxml";
        } else {
            return "EffettuaSegnalazioneView2.fxml";
        }
    }

}

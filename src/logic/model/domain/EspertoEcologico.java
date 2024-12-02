package logic.model.domain;

public class EspertoEcologico extends Utente {
    public EspertoEcologico(int idUtente, String username) {
        super(idUtente, username);
    }

    @Override
    public String getViewIniziale(int interfaccia) {
        if (interfaccia == 1) {
            return "GestisciSegnalazioniView.fxml";
        } else {
            return "GestisciSegnalazioniView2.fxml";
        }
    }
}

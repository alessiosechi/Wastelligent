package model.domain;

public class OperatoreEcologico extends Utente {
    public OperatoreEcologico(int idUtente, String username) {
        super(idUtente, username);
    }

    @Override
    public String getViewIniziale(int interfaccia) {
        if (interfaccia == 1) {
            return "SegnalazioniAssegnateView.fxml";
        } else {
            return "";
        }
    }
}

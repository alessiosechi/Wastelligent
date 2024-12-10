package logic.model.domain;

import java.util.List;

public class OperatoreEcologico extends Utente {
	private List<Segnalazione> segnalazioniAssegnate=null;

	public OperatoreEcologico(int idUtente, String username) {
		super(idUtente, username);
	}
	public OperatoreEcologico(int idUtente, String username, List<Segnalazione> segnalazioniAssegnate) {
		super(idUtente, username);
		this.segnalazioniAssegnate=segnalazioniAssegnate;
	}

	@Override
	public String getViewIniziale(int interfaccia) {
		if (interfaccia == 1) {
			return "SegnalazioniAssegnateView.fxml";
		} else {
			return "";
		}
	}
	
	public List<Segnalazione> getSegnalazioni(){
		return this.segnalazioniAssegnate;
	}

}

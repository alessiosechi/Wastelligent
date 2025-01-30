package logic.model.domain;

import java.util.ArrayList;
import java.util.List;

import logic.observer.Observer;
import logic.observer.Subject;

public class OperatoreEcologico extends Utente implements Subject {
	private List<Observer> osservatori = new ArrayList<>();
	private List<Segnalazione> segnalazioniAssegnate = null;

	public OperatoreEcologico(int idUtente, String username) {
		super(idUtente, username);
	}

	public OperatoreEcologico(int idUtente, String username, List<Segnalazione> segnalazioniAssegnate) {
		super(idUtente, username);
		this.segnalazioniAssegnate = segnalazioniAssegnate;
	}

	@Override
	public String getViewIniziale(int interfaccia) {
		if (interfaccia == 1) {
			return "SegnalazioniAssegnateView.fxml";
		} else {
			return "";
		}
	}

	public List<Segnalazione> getSegnalazioni() {
		return this.segnalazioniAssegnate;
	}

	public void completaSegnalazione(Segnalazione segnalazioneCompletata) {
		int i = segnalazioniAssegnate.indexOf(segnalazioneCompletata);
		if (i >= 0) {
			segnalazioniAssegnate.remove(i);
		}

		notificaOsservatori();
	}

	public void registraOsservatore(Observer observer) {
		osservatori.add(observer);

	}

	public void rimuoviOsservatore(Observer observer) {
		int i = osservatori.indexOf(observer);
		if (i >= 0) {
			osservatori.remove(i);
		}

	}

	public void notificaOsservatori() {
		for (Observer observer : osservatori) {
			observer.update();
		}
	}

}

package logic.model.domain;

import java.util.ArrayList;
import java.util.List;

import logic.observer.Observer;
import logic.observer.Subject2;

public class UtenteBase extends Utente implements Subject2 {
	private List<Observer> osservatori = new ArrayList<>();
	private List<Segnalazione> segnalazioni = null;
	private List<Riscatto> riscatti = null;
	private int punti;

	public UtenteBase(int idUtente, String username) {
		super(idUtente, username);
	}

	public UtenteBase(int idUtente, String username, List<Segnalazione> segnalazioni, List<Riscatto> riscatti,
			int punti) {
		super(idUtente, username);
		this.segnalazioni = segnalazioni;
		this.riscatti = riscatti;
		this.punti = punti;
	}

	@Override
	public String getViewIniziale(int interfaccia) {
		if (interfaccia == 1) {
			return "EffettuaSegnalazioneView.fxml";
		} else {
			return "EffettuaSegnalazioneView2.fxml";
		}
	}

	public List<Segnalazione> getSegnalazioni() {
		return this.segnalazioni;
	}

	public List<Riscatto> getRiscatti() {
		return riscatti;
	}

	public int getPunti() {
		return punti;
	}

	public void setPunti(int punti) {
		this.punti = punti;
	}

	public void aggiungiRiscatto(Riscatto riscatto) {
		riscatti.add(riscatto);
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

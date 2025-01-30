package logic.model.domain;

import java.util.ArrayList;
import java.util.List;

import logic.observer.Observer;
import logic.observer.Subject;

public class SegnalazioniSubject implements Subject {

	protected List<Observer> osservatori = new ArrayList<>();
	protected List<Segnalazione> segnalazioni = new ArrayList<>();

	public void aggiungiSegnalazione(Segnalazione segnalazione) {
		segnalazioni.add(segnalazione);
	}

	public void rimuoviSegnalazione(Segnalazione segnalazione) {
		segnalazioni.remove(segnalazione);
		notificaOsservatori();
	}

	public void setSegnalazioni(List<Segnalazione> segnalazioni) {
		this.segnalazioni = segnalazioni;
	}

	public List<Segnalazione> getSegnalazioni() {
		return segnalazioni;
	}

	@Override
	public void registraOsservatore(Observer observer) {
		osservatori.add(observer);

	}

	@Override
	public void rimuoviOsservatore(Observer observer) {
		int i = osservatori.indexOf(observer);
		if (i >= 0) {
			osservatori.remove(i);
		}

	}

	@Override
	public void notificaOsservatori() {
		for (Observer observer : osservatori) {
			observer.update();
		}
	}
}

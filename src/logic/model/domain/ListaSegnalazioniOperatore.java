package logic.model.domain;

import java.util.ArrayList;
import java.util.List;

import logic.observer.Subject;

public class ListaSegnalazioniOperatore extends Subject {

	private List<Segnalazione> segnalazioni = new ArrayList<>();
	private static ListaSegnalazioniOperatore instance;

	public static ListaSegnalazioniOperatore getInstance() {
		if (instance == null) {
			instance = new ListaSegnalazioniOperatore();
		}
		return instance;
	}

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

}

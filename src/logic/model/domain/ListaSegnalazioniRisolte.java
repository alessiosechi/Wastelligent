package logic.model.domain;

import java.util.ArrayList;
import java.util.List;

import logic.observer.Subject;

public class ListaSegnalazioniRisolte extends Subject {

	private List<Segnalazione> segnalazioni = new ArrayList<>();
	private static ListaSegnalazioniRisolte instance;

	public static ListaSegnalazioniRisolte getInstance() {
		if (instance == null) {
			instance = new ListaSegnalazioniRisolte();
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

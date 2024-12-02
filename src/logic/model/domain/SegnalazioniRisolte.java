package logic.model.domain;

import java.util.ArrayList;
import java.util.List;

import logic.observer.Subject;

public class SegnalazioniRisolte extends Subject {

	private List<Segnalazione> segnalazioni = new ArrayList<>();
	private static SegnalazioniRisolte instance;

	public static SegnalazioniRisolte getInstance() {
		if (instance == null) {
			instance = new SegnalazioniRisolte();
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

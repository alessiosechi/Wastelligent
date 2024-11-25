package logic.model.domain;

import java.util.ArrayList;
import java.util.List;
import logic.observer.Subject;

public class ListaSegnalazioniAttive extends Subject {

	private List<Segnalazione> segnalazioni = new ArrayList<>();
	private static ListaSegnalazioniAttive instance;

	public static ListaSegnalazioniAttive getInstance() {
		if (instance == null) {
			instance = new ListaSegnalazioniAttive();
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

	public List<Segnalazione> getSegnalazioniAttive() {
		return segnalazioni;
	}

}

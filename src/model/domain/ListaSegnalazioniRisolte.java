package model.domain;

import java.util.ArrayList;
import java.util.List;

import logic.observer.Subject;

public class ListaSegnalazioniRisolte extends Subject{
	
	
    private List<Segnalazione> segnalazioniRisolte = new ArrayList<>();
    private static ListaSegnalazioniRisolte instance;


    public static ListaSegnalazioniRisolte getInstance() {
        if (instance == null) {
            instance = new ListaSegnalazioniRisolte();
        }
        return instance;
    }
    
	public void aggiungiSegnalazione(Segnalazione segnalazione) {
		segnalazioniRisolte.add(segnalazione);
	}
	
	public void rimuoviSegnalazione(Segnalazione segnalazione) {
		segnalazioniRisolte.remove(segnalazione);
		notificaOsservatori();
	}
	

    public  List<Segnalazione> getSegnalazioniRisolte() {
        return segnalazioniRisolte;
    }
}

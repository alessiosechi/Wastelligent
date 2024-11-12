package model.domain;

import java.util.ArrayList;
import java.util.List;

import logic.observer.Subject;

public class ListaSegnalazioniAssegnate extends Subject{
	
    private List<Segnalazione> segnalazioniAssegnate = new ArrayList<>();
    private static ListaSegnalazioniAssegnate instance;


    public static ListaSegnalazioniAssegnate getInstance() {
        if (instance == null) {
            instance = new ListaSegnalazioniAssegnate();
        }
        return instance;
    }
    
    

	
	public void rimuoviSegnalazione(Segnalazione segnalazione) {
		segnalazioniAssegnate.remove(segnalazione);
		notificaOsservatori();
	}
	

    public void setSegnalazioniAssegnate(List<Segnalazione> segnalazioni) {
        this.segnalazioniAssegnate=segnalazioni;
    }
	
    public  List<Segnalazione> getSegnalazioniAssegnate() {
        return segnalazioniAssegnate;
    }
}

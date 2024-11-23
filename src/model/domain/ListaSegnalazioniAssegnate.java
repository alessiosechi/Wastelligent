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
    
	public void aggiungiSegnalazione(Segnalazione segnalazione) {
		segnalazioniAssegnate.add(segnalazione);
	}

	
	public void rimuoviSegnalazione(Segnalazione segnalazione) {
		segnalazioniAssegnate.remove(segnalazione);
		notificaOsservatori();
	}
	
	
	
    public  List<Segnalazione> getSegnalazioniAssegnate() {
        return segnalazioniAssegnate;
    }
    
    
    
    
    // Metodo per ottenere segnalazioni assegnate a un operatore specifico
    public List<Segnalazione> getSegnalazioniPerOperatore(int idOperatore) {
        List<Segnalazione> segnalazioniFiltrate = new ArrayList<>();
        for (Segnalazione segnalazione : segnalazioniAssegnate) {
            if (segnalazione.getIdOperatore() == idOperatore) {
                segnalazioniFiltrate.add(segnalazione);
            }
        }
        return segnalazioniFiltrate;
    }

}

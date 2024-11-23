package model.domain;

import java.util.ArrayList;
import java.util.List;
import logic.observer.Subject;

public class ListaSegnalazioniAttive extends Subject{
	

	
	
    private List<Segnalazione> segnalazioniAttive = new ArrayList<>();
    private static ListaSegnalazioniAttive instance;


    public static ListaSegnalazioniAttive getInstance() {
        if (instance == null) {
            instance = new ListaSegnalazioniAttive();
        }
        return instance;
    }
    
    
	public void aggiungiSegnalazione(Segnalazione segnalazione) {
		segnalazioniAttive.add(segnalazione);
	}
	
	public void rimuoviSegnalazione(Segnalazione segnalazione) {
		segnalazioniAttive.remove(segnalazione);
		notificaOsservatori();
	}
	

	
    public  List<Segnalazione> getSegnalazioniAttive() {
        return segnalazioniAttive;
    }




}

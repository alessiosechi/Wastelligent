package model.domain;

import java.util.ArrayList;
import java.util.List;

public class ListaSegnalazioniRiscontrate {
    private List<Segnalazione> segnalazioniRiscontrate = new ArrayList<>();
    
    private static ListaSegnalazioniRiscontrate instance;


    public static ListaSegnalazioniRiscontrate getInstance() {
        if (instance == null) {
            instance = new ListaSegnalazioniRiscontrate();
        }
        return instance;
    }
    
    
	public void aggiungiSegnalazione(Segnalazione segnalazione) {
		segnalazioniRiscontrate.add(segnalazione);
	}

	
    public  List<Segnalazione> getSegnalazioniRiscontrate() {
        return segnalazioniRiscontrate;
    }
}

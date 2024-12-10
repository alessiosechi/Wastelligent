package logic.model.domain;

import java.util.ArrayList;
import java.util.List;

import logic.observer.Subject;

public abstract class SegnalazioniSubject extends Subject {
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
}


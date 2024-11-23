package model.dao;

import java.util.ArrayList;
import java.util.List;

import model.domain.ListaSegnalazioniAssegnate;
import model.domain.ListaSegnalazioniAttive;
import model.domain.ListaSegnalazioniRiscontrate;
import model.domain.ListaSegnalazioniRisolte;
import model.domain.Segnalazione;
import model.domain.StatoSegnalazione;

public class SegnalazioneDaoInMemory implements SegnalazioneDao{
	
	
    private static ListaSegnalazioniAttive segnalazioniAttive = ListaSegnalazioniAttive.getInstance();
    private static ListaSegnalazioniAssegnate segnalazioniAssegnate = ListaSegnalazioniAssegnate.getInstance();
    private static ListaSegnalazioniRisolte segnalazioniRisolte = ListaSegnalazioniRisolte.getInstance();
    private static ListaSegnalazioniRiscontrate segnalazioniRiscontrate = ListaSegnalazioniRiscontrate.getInstance();
    
    private static int idSegnalazioneCounter = 0;
    
	@Override
	public void salvaSegnalazione(Segnalazione segnalazione) {
		idSegnalazioneCounter++;
		segnalazione.setIdSegnalazione(idSegnalazioneCounter);
        segnalazioniAttive.aggiungiSegnalazione(segnalazione);	
	}

	@Override
	public void eliminaSegnalazione(Segnalazione segnalazione) {
		segnalazioniAttive.rimuoviSegnalazione(segnalazione);
		
	}

	@Override
	public void aggiornaStato(int idSegnalazione, String stato) {
	    Segnalazione segnalazione = trovaSegnalazionePerId(idSegnalazione);
	    if (segnalazione == null) {
	        throw new IllegalArgumentException("Segnalazione non trovata!");
	    }
	    segnalazione.setStato(stato);
        segnalazioniAssegnate.rimuoviSegnalazione(segnalazione);
        segnalazioniRisolte.aggiungiSegnalazione(segnalazione);
		
	}

	@Override
	public List<Segnalazione> trovaSegnalazioniRiscontrate(int idUtente) {

        List<Segnalazione> segnalazioniFiltrate = new ArrayList<>();
        for (Segnalazione segnalazione : segnalazioniRiscontrate.getSegnalazioniRiscontrate()) {
            if (segnalazione.getIdUtente() == idUtente) {
                segnalazioniFiltrate.add(segnalazione);
            }
        }

        return segnalazioniFiltrate;
	}


    @Override
    public List<Segnalazione> getSegnalazioniByStato(String stato) {
        if (StatoSegnalazione.RICEVUTA.getStato().equals(stato)) {
            return ListaSegnalazioniAttive.getInstance().getSegnalazioniAttive();
        } else if (StatoSegnalazione.IN_CORSO.getStato().equals(stato)) {
            return ListaSegnalazioniAssegnate.getInstance().getSegnalazioniAssegnate();
        }
        else if (StatoSegnalazione.RISOLTA.getStato().equals(stato)) {
            return ListaSegnalazioniRisolte.getInstance().getSegnalazioniRisolte();
        }
        else if (StatoSegnalazione.RISCONTRATA.getStato().equals(stato)) {
            return ListaSegnalazioniRiscontrate.getInstance().getSegnalazioniRiscontrate();
        }

        return new ArrayList<>(); // vuoto se lo stato non corrisponde a una lista specifica
    }

    @Override
    public List<Segnalazione> getSegnalazioniAssegnate(int idOperatore, String stato) {
        List<Segnalazione> segnalazioniByStato = getSegnalazioniByStato(stato);

        List<Segnalazione> segnalazioniFiltrate = new ArrayList<>();
        for (Segnalazione segnalazione : segnalazioniByStato) {
            if (segnalazione.getIdOperatore() == idOperatore) {
                segnalazioniFiltrate.add(segnalazione);
            }
        }

        return segnalazioniFiltrate;
    }

	@Override
	public void assegnaOperatore(int idSegnalazione, int idOperatore, int idEsperto) {
		
		
	    Segnalazione segnalazione = trovaSegnalazionePerId(idSegnalazione);
	    if (segnalazione == null) {
	        throw new IllegalArgumentException("Segnalazione non trovata!");
	    }
	    segnalazione.setIdOperatore(idOperatore);
    	segnalazioniAttive.rimuoviSegnalazione(segnalazione);
    	segnalazioniAssegnate.aggiungiSegnalazione(segnalazione);
	}

	@Override
	public void assegnaPunti(int idSegnalazione, int punti) {
		
	    Segnalazione segnalazione = trovaSegnalazionePerId(idSegnalazione);
	    if (segnalazione == null) {
	        throw new IllegalArgumentException("Segnalazione non trovata!");
	    }
	    
	    segnalazione.setPuntiAssegnati(punti);
    	segnalazioniRisolte.rimuoviSegnalazione(segnalazione);
    	segnalazioniRiscontrate.aggiungiSegnalazione(segnalazione);
		
	}

    private Segnalazione trovaSegnalazionePerId(int idSegnalazione) {
        for (Segnalazione segnalazione : segnalazioniAttive.getSegnalazioniAttive()) {
            if (segnalazione.getIdSegnalazione() == idSegnalazione) {
                return segnalazione;
            }
        }

        for (Segnalazione segnalazione : segnalazioniAssegnate.getSegnalazioniAssegnate()) {
            if (segnalazione.getIdSegnalazione() == idSegnalazione) {
                return segnalazione;
            }
        }

        for (Segnalazione segnalazione : segnalazioniRisolte.getSegnalazioniRisolte()) {
            if (segnalazione.getIdSegnalazione() == idSegnalazione) {
                return segnalazione;
            }
        }

        for (Segnalazione segnalazione : segnalazioniRiscontrate.getSegnalazioniRiscontrate()) {
            if (segnalazione.getIdSegnalazione() == idSegnalazione) {
                return segnalazione;
            }
        }

        return null;
    }

}

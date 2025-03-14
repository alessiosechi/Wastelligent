package logic.model.dao;

import java.util.ArrayList;
import java.util.List;

import logic.model.domain.Segnalazione;
import logic.model.domain.StatoSegnalazione;

public class SegnalazioneDaoInMemory implements SegnalazioneDao {

	private static final String ERRORE_SEGNALAZIONE_NON_TROVATA = "Segnalazione non trovata!";
	private static List<Segnalazione> segnalazioni = new ArrayList<>();
	private int idSegnalazioneCounter = 0;

	@Override
	public void salvaSegnalazione(Segnalazione segnalazione) {
		idSegnalazioneCounter++;
		segnalazione.setIdSegnalazione(idSegnalazioneCounter);
		segnalazioni.add(segnalazione);
	}

	@Override
	public void eliminaSegnalazione(Segnalazione segnalazione) {
		segnalazioni.remove(segnalazione);
	}

	@Override
	public void aggiornaStato(int idSegnalazione, String stato) {
		Segnalazione segnalazione = trovaSegnalazionePerId(idSegnalazione);
		if (segnalazione == null) {
			throw new IllegalArgumentException(ERRORE_SEGNALAZIONE_NON_TROVATA);
		}
		segnalazione.setStato(stato);
	}

	@Override
	public List<Segnalazione> getSegnalazioniRiscontrateByUtente(int idUtente) {
		List<Segnalazione> segnalazioniFiltrate = new ArrayList<>();
		for (Segnalazione segnalazione : segnalazioni) {
			if (segnalazione.getIdUtente() == idUtente
					&& StatoSegnalazione.RISCONTRATA.getStato().equals(segnalazione.getStato())) {
				segnalazioniFiltrate.add(segnalazione);
			}
		}
		return segnalazioniFiltrate;
	}

	@Override
	public List<Segnalazione> getSegnalazioniByStato(String stato) {
		List<Segnalazione> segnalazioniFiltrate = new ArrayList<>();
		for (Segnalazione segnalazione : segnalazioni) {
			if (segnalazione.getStato().equals(stato)) {
				segnalazioniFiltrate.add(segnalazione);
			}
		}
		return segnalazioniFiltrate;
	}

	@Override
	public List<Segnalazione> getSegnalazioniAssegnate(int idOperatore) {
		List<Segnalazione> segnalazioniOperatore = new ArrayList<>();
		for (Segnalazione segnalazione : segnalazioni) {
			if (segnalazione.getIdOperatore() == idOperatore
					&& StatoSegnalazione.IN_CORSO.getStato().equals(segnalazione.getStato())) {
				segnalazioniOperatore.add(segnalazione);
			}
		}
		return segnalazioniOperatore;
	}

	@Override
	public void assegnaOperatore(int idSegnalazione, int idOperatore) {
		Segnalazione segnalazione = trovaSegnalazionePerId(idSegnalazione);
		if (segnalazione == null) {
			throw new IllegalArgumentException(ERRORE_SEGNALAZIONE_NON_TROVATA);
		}
		segnalazione.setIdOperatore(idOperatore);
	}

	@Override
	public void assegnaPunti(int idSegnalazione, int punti) {
		Segnalazione segnalazione = trovaSegnalazionePerId(idSegnalazione);
		if (segnalazione == null) {
			throw new IllegalArgumentException(ERRORE_SEGNALAZIONE_NON_TROVATA);
		}
		segnalazione.setPuntiAssegnati(punti);
	}

	private Segnalazione trovaSegnalazionePerId(int idSegnalazione) {
		for (Segnalazione segnalazione : segnalazioni) {
			if (segnalazione.getIdSegnalazione() == idSegnalazione) {
				return segnalazione;
			}
		}
		return null;
	}

}

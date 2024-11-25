package logic.model.dao;

import java.util.List;

import logic.model.domain.Segnalazione;

public interface SegnalazioneDao {
	void salvaSegnalazione(Segnalazione segnalazione);
	void eliminaSegnalazione(Segnalazione segnalazione);
    void aggiornaStato(int idSegnalazione, String stato);
	
	List<Segnalazione> trovaSegnalazioniRiscontrate(int idUtente);
	List<Segnalazione> getSegnalazioniByStato(String stato);
	List<Segnalazione> getSegnalazioniAssegnate(int idOperatore, String stato);
	

	
    void assegnaOperatore(int idSegnalazione, int idOperatore);
    void assegnaPunti(int idSegnalazione, int punti);
    
}

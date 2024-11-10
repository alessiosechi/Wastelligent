package model.dao;

import model.domain.Segnalazione;
import java.util.List;

public interface SegnalazioneDAO {
	void salvaSegnalazione(Segnalazione segnalazione);
	void eliminaSegnalazione(int idSegnalazione);
    void aggiornaStato(int idSegnalazione, String stato);
	
	List<Segnalazione> trovaSegnalazioniRiscontrate(int idUtente);
	List<Segnalazione> getSegnalazioni();
	List<Segnalazione> getSegnalazioniByStato(String stato);
	List<Segnalazione> getSegnalazioniAssegnate(int idOperatore, String stato);
	

	
    void assegnaOperatore(int idSegnalazione, int idOperatore, int idEsperto);
    void assegnaPunti(int idSegnalazione, int punti);
    
}

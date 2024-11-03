package model.dao;

import model.domain.Segnalazione;

import java.sql.SQLException;
import java.util.List;

public interface SegnalazioneDAO {
	void salvaSegnalazione(Segnalazione segnalazione) throws SQLException;
	List<Segnalazione> trovaSegnalazioniRiscontrate(int idUtente)throws SQLException;
	List<Segnalazione> getSegnalazioni()throws SQLException;
	
	List<Segnalazione> getSegnalazioniByStato(String stato)throws SQLException;
	
	void eliminaSegnalazione(int idSegnalazione) throws SQLException;
	
    void assegnaOperatore(int idSegnalazione, int idOperatore, int idEsperto);
    void assegnaPunti(int idSegnalazione, int punti) throws SQLException;
    

    
	List<Segnalazione> getSegnalazioniAssegnate(int idOperatore, String stato)throws SQLException;
	
    void aggiornaStato(int idSegnalazione)throws SQLException;

}

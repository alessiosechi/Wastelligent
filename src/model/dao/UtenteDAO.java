package model.dao;

import java.util.List;

import model.domain.OperatoreEcologico;

public interface UtenteDao {
    int estraiPuntiUtente(int idUtente);
    void aggiungiPuntiUtente(int idUtente, int puntiDaAggiungere);
    void sottraiPuntiUtente(int idUtente, int puntiDaSottrarre);
    
    List<OperatoreEcologico> estraiOperatoriEcologici();

}

package logic.model.dao;


public interface UtenteBaseDao {
    int estraiPunti(int idUtente);
    void aggiungiPunti(int idUtente, int puntiDaAggiungere);
    void sottraiPunti(int idUtente, int puntiDaSottrarre);
    

}

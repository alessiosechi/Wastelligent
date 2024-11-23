package model.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.domain.ListaOperatoriEcologici;
import model.domain.OperatoreEcologico;

public class UtenteDaoInMemory implements UtenteDao {

    // mappa per tenere traccia dei punti degli utenti
    private Map<Integer, Integer> puntiUtenti;

    public UtenteDaoInMemory() {
        this.puntiUtenti = new HashMap<>();
    }

    @Override
    public int estraiPuntiUtente(int idUtente) {
        return puntiUtenti.getOrDefault(idUtente, 0); // restituisco i punti dell'utente, se non esistono restituisce 0
    }

    @Override
    public void aggiungiPuntiUtente(int idUtente, int puntiDaAggiungere) {
        int puntiAttuali = puntiUtenti.getOrDefault(idUtente, 0);
        puntiUtenti.put(idUtente, puntiAttuali + puntiDaAggiungere);
    }

    @Override
    public void sottraiPuntiUtente(int idUtente, int puntiDaSottrarre) {
        int puntiAttuali = puntiUtenti.getOrDefault(idUtente, 0);
        puntiUtenti.put(idUtente, puntiAttuali - puntiDaSottrarre);

    }

    @Override
    public List<OperatoreEcologico> estraiOperatoriEcologici() {
        return ListaOperatoriEcologici.getInstance().getOperatoriEcologici();
    }
}

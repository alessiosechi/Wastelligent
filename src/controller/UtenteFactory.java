package controller;

import model.domain.Utente;
import model.domain.UtenteBase;

import model.domain.EspertoEcologico;
import model.domain.OperatoreEcologico;
import model.domain.Ruolo;

public class UtenteFactory {
    public Utente createUtente(int idUtente, String username, Ruolo ruolo) {
        switch (ruolo) {
            case UTENTE_BASE:
                return new UtenteBase(idUtente, username);
            case ESPERTO_ECOLOGICO:
                return new EspertoEcologico(idUtente, username);
            case OPERATORE_ECOLOGICO:
                return new OperatoreEcologico(idUtente, username);
            default:
                throw new IllegalArgumentException("Ruolo non valido: " + ruolo);
        }
    }
    
   
}
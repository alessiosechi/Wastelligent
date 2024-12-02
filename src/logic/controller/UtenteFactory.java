package logic.controller;

import logic.model.domain.EspertoEcologico;
import logic.model.domain.OperatoreEcologico;
import logic.model.domain.Ruolo;
import logic.model.domain.Utente;
import logic.model.domain.UtenteBase;

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
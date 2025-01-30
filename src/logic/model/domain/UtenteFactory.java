package logic.model.domain;

public class UtenteFactory {
	
    private static UtenteFactory instance = null;

    public static UtenteFactory getInstance() {
        if (instance == null) {
            instance = new UtenteFactory();
        }
        return instance;
    }
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
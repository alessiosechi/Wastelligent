package model.domain;

public class UtenteCorrente {
	
    private static UtenteCorrente instance;
    private Utente utente;

    private UtenteCorrente() {}

    public static UtenteCorrente getInstance() {
        if (instance == null) {
            instance = new UtenteCorrente();
        }
        return instance;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

//    public void clear() {
//        this.utente = null;
//    }
}

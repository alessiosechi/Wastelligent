package model.domain;

public abstract class Utente  { 	
    private int idUtente;
    private String username;

    protected Utente(int idUtente, String username) {
        this.idUtente = idUtente;
        this.username = username;
    }
    
    // comportamenti comuni
    public int getIdUtente() {
        return idUtente;
    }

    public String getUsername() {
        return username;
    }
    
	public abstract String getViewIniziale(int interfacciaSelezionata);
}

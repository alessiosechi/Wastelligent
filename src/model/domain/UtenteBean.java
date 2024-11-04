package model.domain;

public class UtenteBean { 
    private int idUtente;
	private String username;  
	
	public UtenteBean() {
        // Costruttore vuoto
	}

    public void setIdUtente(int idUtente) {
        this.idUtente= idUtente;
    }
    public int getIdUtente() {
        return idUtente;
    }
	public void setUsername(String username) { 
		this.username=username;
	}
	
	public String getUsername() {
		return username;
	}
}

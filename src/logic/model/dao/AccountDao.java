package logic.model.dao;

public interface AccountDao { // forse è meglio "AccountDao"?
	public int autenticazione(String username, String password);
    public int getIdByUsername(String username);
    
    public boolean registraUtente(String username, String password);
    public boolean isUsernameTaken(String username);
}

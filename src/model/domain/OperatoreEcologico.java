package model.domain;

public class OperatoreEcologico implements Utente{
    private int idUtente;
	private String username;

	
	public OperatoreEcologico(int idUtente, String username) {
		this.idUtente=idUtente;
		this.username=username;

	}
    @Override
    public int getIdUtente() {
        return idUtente;
    }
	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public String getViewIniziale(int interfaccia) {
		if(interfaccia==1)
		{
			return "SegnalazioniAssegnateView.fxml"; 
		}
		else {
			return ""; 
		}

	}

}

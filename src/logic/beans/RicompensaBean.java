package logic.beans;

public class RicompensaBean {

	private int idRicompensa;
	private String nome;
	private int valore;
	private String descrizione;
	private String dataScadenza;

	public int getIdRicompensa() {
		return idRicompensa;
	}

	public void setIdRicompensa(int idRicompensa) {
		this.idRicompensa = idRicompensa;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getValore() {
		return valore;
	}

	public void setValore(int valore) {
		this.valore = valore;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(String dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

}

package model.domain;

public class RicompensaBean {

	private int idUtente;
	private int idRicompensa;
	private String nome;
	private String codiceRiscatto;
	private int valore;
	private String descrizione;
	private String dataScadenza;
	private String dataRiscatto;
	private int punti;

	public RicompensaBean() {

	}

	public int getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(int idUtente) {
		this.idUtente = idUtente;
	}

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

	public String getCodiceRiscatto() {
		return codiceRiscatto;
	}

	public void setCodiceRiscatto(String codiceRiscatto) {
		this.codiceRiscatto = codiceRiscatto;
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

	public String getDataRiscatto() {
		return dataRiscatto;
	}

	public void setDataRiscatto(String dataRiscatto) {
		this.dataRiscatto = dataRiscatto;
	}

	public int getPunti() {
		return punti;
	}

	public void setPunti(int punti) {
		this.punti = punti;
	}

}

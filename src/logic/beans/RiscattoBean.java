package logic.beans;

public class RiscattoBean {
	private RicompensaBean ricompensaBean;
	private int idUtente;
	private int punti;
	private String codiceRiscatto;
	private String dataRiscatto;

	public int getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(int idUtente) {
		this.idUtente = idUtente;
	}

	public int getPunti() {
		return punti;
	}

	public void setPunti(int punti) {
		this.punti = punti;
	}

	public String getCodiceRiscatto() {
		return codiceRiscatto;
	}

	public void setCodiceRiscatto(String codiceRiscatto) {
		this.codiceRiscatto = codiceRiscatto;
	}

	public String getDataRiscatto() {
		return dataRiscatto;
	}

	public void setDataRiscatto(String dataRiscatto) {
		this.dataRiscatto = dataRiscatto;
	}

	public void setRicompensaBean(RicompensaBean ricompensaBean) {
		this.ricompensaBean = ricompensaBean;
	}

	public String getNomeRicompensa() {
		return ricompensaBean.getNome();
	}

	public String getDescrizioneRicompensa() {
		return ricompensaBean.getDescrizione();
	}

	public int getValoreRicompensa() {
		return ricompensaBean.getValore();
	}

	public String getDataScadenzaRicompensa() {
		return ricompensaBean.getDataScadenza();
	}

}

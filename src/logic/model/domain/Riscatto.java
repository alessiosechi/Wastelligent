package logic.model.domain;

import java.io.Serializable;

public class Riscatto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Ricompensa ricompensa;
	private int idUtente;
	private int punti;
	private String codiceRiscatto;
	private String dataRiscatto;

	public Riscatto(Ricompensa ricompensa, int idUtente, int punti, String codiceRiscatto, String dataRiscatto) {
		this.ricompensa = ricompensa;
		this.idUtente = idUtente;
		this.codiceRiscatto = codiceRiscatto;
		this.dataRiscatto = dataRiscatto;
		this.punti = punti;
	}

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

	public void setRicompensa(Ricompensa ricompensa) {
		this.ricompensa = ricompensa;
	}
	/*
	 * non metto il getter per la Ricompensa altrimenti violerei la legge di Demetra
	 */

	public String getNomeRicompensa() {
		return ricompensa.getNome();
	}

	public String getDescrizioneRicompensa() {
		return ricompensa.getDescrizione();
	}

	public int getValoreRicompensa() {
		return ricompensa.getValore();
	}

	public String getDataScadenzaRicompensa() {
		return ricompensa.getDataScadenza();
	}

	@Override
	public String toString() {
		return "Riscatto {" + "Ricompensa {" + "Nome='" + getNomeRicompensa() + '\'' + ", Descrizione='"
				+ getDescrizioneRicompensa() + '\'' + ", Valore=" + getValoreRicompensa() + ", Data Scadenza='"
				+ getDataScadenzaRicompensa() + '\'' + "}, " + "idUtente=" + idUtente + ", Punti=" + punti
				+ ", Codice Riscatto='" + codiceRiscatto + '\'' + ", Data Riscatto='" + dataRiscatto + '\'' + '}';
	}

}

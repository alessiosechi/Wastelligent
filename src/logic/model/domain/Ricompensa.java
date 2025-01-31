
package logic.model.domain;

import java.io.Serializable;

public class Ricompensa implements Serializable {

	private static final long serialVersionUID = 6800466097865239661L;
	private int idRicompensa;
	private String nome;
	private int valore;
	private String descrizione;
	private String dataScadenza;

	public Ricompensa() {
		// Costruttore vuoto
	}

	// overloading dei costruttori

	// costruttore per la lista di ricompense dall'API
	public Ricompensa(int idRicompensa, String nome, int valore, String descrizione, String dataScadenza) {
		this.idRicompensa = idRicompensa;
		this.nome = nome;
		this.descrizione = descrizione;
		this.valore = valore;
		this.dataScadenza = dataScadenza;
	}

	// costruttore utile per comporre l'oggetto Riscatto
	public Ricompensa(String nome, int valore, String descrizione, String dataScadenza) {
		this.nome = nome;
		this.descrizione = descrizione;
		this.valore = valore;
		this.dataScadenza = dataScadenza;
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

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public int getValore() {
		return valore;
	}

	public void setValore(int valore) {
		this.valore = valore;
	}

	public String getDataScadenza() {
		return dataScadenza;
	}

	public void setDataScadenza(String dataScadenza) {
		this.dataScadenza = dataScadenza;
	}

}

package logic.model.domain;

public class SegnalazioneBean {
	private int idUtente;
	private String descrizione;
	private String percorsoFoto;
    private String stato;
	private double latitudine;
	private double longitudine;
	
	private int puntiAssegnati;
	private String posizione;
	
	private int idSegnalazione;
	
	private int idOperatore;
	

	public SegnalazioneBean() {
        // Costruttore vuoto
	}

	public int getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(int idUtente) {
		this.idUtente = idUtente;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getFoto() {
		return percorsoFoto;
	}

	public void setFoto(String percorsoFoto) {
		this.percorsoFoto = percorsoFoto;
	}
	
	
	
    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }
	

	public double getLatitudine() {
		return latitudine;
	}

	public void setLatitudine(double latitudine) {
		this.latitudine = latitudine;
	}

	public double getLongitudine() {
		return longitudine;
	}

	public void setLongitudine(double longitudine) {
		this.longitudine = longitudine;
	}
	
	
	
	
	
	
    public int getPuntiAssegnati() {
        return puntiAssegnati;
    }

    public void setPuntiAssegnati(int puntiAssegnati) {
        this.puntiAssegnati = puntiAssegnati;
    }

    
    public String getPosizione() {
        return posizione;
    }

    public void setPosizione(String posizione) {
        this.posizione = posizione;
    }
    
    
    
    
	public int getIdSegnalazione() {
		return idSegnalazione;
	}

	public void setIdSegnalazione(int idSegnalazione) {
		this.idSegnalazione = idSegnalazione;
	}
	
	
	
    public int getIdOperatore() {
        return idOperatore;
    }

    public void setIdOperatore(int idOperatore) {
        this.idOperatore = idOperatore;
    }
	
	
	
	

}

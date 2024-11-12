package model.domain;

import java.util.Objects;

// a ogni classe entità dovrebbe corrispondere un DAO

public class Segnalazione {

	private int idSegnalazione;
    private String descrizione;
    private String foto; // percorso della foto
    private String stato;
    private int idUtente;
    private double latitudine;
    private double longitudine;
    
    private int puntiAssegnati;
    private String posizione;
    

    private int idOperatore;
    
    public Segnalazione() {
        // Costruttore vuoto
    }


    // Sovrascrivi il metodo equals per confrontare le segnalazioni tramite l'idSegnalazione
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Segnalazione that = (Segnalazione) o;
        return idSegnalazione == that.idSegnalazione;
    }

    // Sovrascrivi anche il metodo hashCode per mantenere il contratto con equals
    @Override
    public int hashCode() {
        return Objects.hash(idSegnalazione);
    }


    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }



    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
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

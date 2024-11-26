
package logic.model.domain;

public class Ricompensa {

    private int idUtente;
    private int idRicompensa;
    
    private String nome;
    private String descrizione;
    private int valore;
    private String codiceRiscatto;
    private String dataRiscatto;
    private String dataScadenza;
    private int punti;


    public Ricompensa() {
        // Costruttore vuoto
    }
    
    
    // overloading dei costruttori
    
    
    // costruttore per la lista di ricompense dall'API
    public Ricompensa(int idRicompensa, String nome,int valore, String descrizione, String dataScadenza) {
    	this.idRicompensa=idRicompensa;
        this.nome = nome;
        this.descrizione = descrizione;
        this.valore = valore;
        this.dataScadenza = dataScadenza;

    }

    // costruttore per la lista di ricompense riscattate dall'utente
    public Ricompensa(String nome, String descrizione, int valore, String codiceRiscatto, String dataRiscatto, String dataScadenza, int punti) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.valore = valore;
        this.codiceRiscatto = codiceRiscatto;
        this.dataRiscatto = dataRiscatto;
        this.dataScadenza = dataScadenza;
        this.punti = punti;
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

    public String getDataScadenza() {
        return dataScadenza;
    }

    public void setDataScadenza(String dataScadenza) {
        this.dataScadenza = dataScadenza;
    }

    public int getPunti() {
        return punti;
    }

    public void setPunti(int punti) {
        this.punti = punti;
    }
}

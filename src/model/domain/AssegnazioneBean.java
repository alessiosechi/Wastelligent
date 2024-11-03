package model.domain;

public class AssegnazioneBean {
	
    private SegnalazioneBean segnalazione;
    private UtenteBean operatore;
    private UtenteBean esperto;
    
    public SegnalazioneBean getSegnalazione() {
        return segnalazione;
    }

    public void setSegnalazione(SegnalazioneBean segnalazione) {
        this.segnalazione = segnalazione;
    }

    public UtenteBean getOperatore() {
        return operatore;
    }

    public void setOperatore(UtenteBean operatore) {
        this.operatore = operatore;
    }
    public UtenteBean getEsperto() {
        return esperto;
    }

    public void setEsperto(UtenteBean esperto) {
        this.esperto = esperto;
    }
}

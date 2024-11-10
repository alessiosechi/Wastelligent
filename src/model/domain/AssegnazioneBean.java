package model.domain;

public class AssegnazioneBean { // DA RIVEDERE BENE!
	
    private SegnalazioneBean segnalazione;
    private UtenteBean operatore;
    
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

}

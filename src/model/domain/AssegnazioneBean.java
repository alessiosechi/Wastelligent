package model.domain;

public class AssegnazioneBean { // DA RIVEDERE BENE!
	
    private SegnalazioneBean segnalazione;
    private OperatoreEcologicoBean operatore;
    
    public SegnalazioneBean getSegnalazione() {
        return segnalazione;
    }

    public void setSegnalazione(SegnalazioneBean segnalazione) {
        this.segnalazione = segnalazione;
    }

    public OperatoreEcologicoBean getOperatore() {
        return operatore;
    }

    public void setOperatore(OperatoreEcologicoBean operatore) {
        this.operatore = operatore;
    }

}

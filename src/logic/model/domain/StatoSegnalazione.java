package logic.model.domain;

public enum StatoSegnalazione {
    RICEVUTA("Ricevuta"),
    IN_CORSO("In corso"),
    RISOLTA("Risolta"),
    RISCONTRATA("Riscontrata");
	
    private final String stato; // variabile immutabile per ciascuna costante

    StatoSegnalazione(String stato) {
        this.stato = stato;
    }

    public String getStato() {
        return stato;
    }
}

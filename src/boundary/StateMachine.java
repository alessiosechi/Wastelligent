package boundary;

public class StateMachine {

    private State currentState;  // stato corrente
    private final DettagliSegnalazioneViewController controller;

    public StateMachine( ) {
        this.controller = DettagliSegnalazioneViewController.getInstance();;
    }


    public void setState(State state) {
        this.currentState = state;
        if (currentState != null) {
            currentState.configureView(controller);  // configuro la view in base allo stato attuale
        } else {
            // se state è null:
            System.err.println("Errore: stato non valido.");
        }
    }
}

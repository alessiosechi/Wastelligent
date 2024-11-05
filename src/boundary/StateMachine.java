package boundary;

import java.util.logging.Logger;

public class StateMachine {

    private State currentState;  // stato corrente
    private final DettagliSegnalazioneViewController controller;
	private static final Logger logger = Logger.getLogger(StateMachine.class.getName());

    public StateMachine( ) {
        this.controller = DettagliSegnalazioneViewController.getInstance();
    }


    public void setState(State state) {
        this.currentState = state;
        if (currentState != null) {
            currentState.configureView(controller);  // configuro la view in base allo stato attuale
        } else {
            // se state è null:
            logger.severe("Errore: stato non valido.");
        }
    }
}

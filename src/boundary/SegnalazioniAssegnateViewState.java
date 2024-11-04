package boundary;

import java.util.logging.Logger;

public class SegnalazioniAssegnateViewState implements State {
	private static final Logger logger = Logger.getLogger(SegnalazioniAssegnateViewState.class.getName());

	@Override
	public void configureView(DettagliSegnalazioneViewController dettagliSegnalazioneViewController) {

		logger.info("Configurazione DettagliSegnalazione per SegnalazioniAssegnateView");

		dettagliSegnalazioneViewController.indietroButton.setOnAction(event -> 

			ViewLoader.caricaView("SegnalazioniAssegnateView.fxml",
					dettagliSegnalazioneViewController.getPrimaryStage())
		);

		dettagliSegnalazioneViewController.button2.setDisable(true);
		dettagliSegnalazioneViewController.button2.setVisible(false);

		dettagliSegnalazioneViewController.button1.setLayoutX(537.375);
		dettagliSegnalazioneViewController.line.setLayoutX(537.375);
		dettagliSegnalazioneViewController.button1.setText("VISUALIZZA\nASSEGNAZIONI");

		dettagliSegnalazioneViewController.button1.setOnAction(event -> 
			ViewLoader.caricaView("SegnalazioniAssegnateView.fxml",
					dettagliSegnalazioneViewController.getPrimaryStage())
		);

	}

}

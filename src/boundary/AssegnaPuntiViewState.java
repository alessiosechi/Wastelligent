package boundary;

import java.util.logging.Logger;



public class AssegnaPuntiViewState implements State {
	private static final Logger logger = Logger.getLogger(AssegnaPuntiViewState.class.getName());

	@Override
	public void configureView(DettagliSegnalazioneViewController dettagliSegnalazioneViewController) {


		
		
		logger.info("Configurazione DettagliSegnalazione per AssegnaPuntiView");

		dettagliSegnalazioneViewController.indietroButton.setOnAction(event -> 
			ViewLoader.caricaView(ViewInfo.ASSEGNA_PUNTI_VIEW, dettagliSegnalazioneViewController.getPrimaryStage())
		);

		dettagliSegnalazioneViewController.button1.setOnAction(event -> 
			ViewLoader.caricaView(ViewInfo.GESTISCI_SEGNALAZIONI_VIEW,
					dettagliSegnalazioneViewController.getPrimaryStage())
		);

		dettagliSegnalazioneViewController.button2.setOnAction(event -> 
			ViewLoader.caricaView(ViewInfo.ASSEGNA_PUNTI_VIEW, dettagliSegnalazioneViewController.getPrimaryStage())

		);

		dettagliSegnalazioneViewController.button1.setText("GESTISCI\nSEGNALAZIONI");
		dettagliSegnalazioneViewController.button2.setText("ASSEGNA\nPUNTI");

		dettagliSegnalazioneViewController.line.setLayoutX(645.0);

	}
}

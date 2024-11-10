package boundary;

import java.util.logging.Logger;

public class StoricoViewState implements State {
	private static final Logger logger = Logger.getLogger(StoricoViewState.class.getName());

	@Override
	public void configureView(DettagliSegnalazioneViewController dettagliSegnalazioneViewController) {

		logger.info("Configurazione DettagliSegnalazione per StoricoView");
		dettagliSegnalazioneViewController.indietroButton.setOnAction(event -> 

			ViewLoader.caricaView(ViewInfo.STORICO_VIEW, dettagliSegnalazioneViewController.getPrimaryStage())
		);

		dettagliSegnalazioneViewController.button1.setOnAction(event -> 
			ViewLoader.caricaView(ViewInfo.EFFETTUA_SEGNALAZIONE_VIEW,
					dettagliSegnalazioneViewController.getPrimaryStage())
		);

		dettagliSegnalazioneViewController.button2.setOnAction(event -> 
			ViewLoader.caricaView(ViewInfo.RISCATTA_RICOMPENSA_VIEW, dettagliSegnalazioneViewController.getPrimaryStage())
		);

		dettagliSegnalazioneViewController.button1.setText("NUOVA\nSEGNALAZIONE");
		dettagliSegnalazioneViewController.button2.setText("RISCATTA\nRICOMPENSA");
		dettagliSegnalazioneViewController.line.setLayoutX(645.0);
	}
}
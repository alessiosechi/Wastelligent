package boundary;

public class StoricoViewState implements State {

	@Override
	public void configureView(DettagliSegnalazioneViewController dettagliSegnalazioneViewController) {

		System.out.println("Configurazione DettagliSegnalazione per StoricoView");
		dettagliSegnalazioneViewController.indietroButton.setOnAction(event -> {

			ViewLoader.caricaView("StoricoView.fxml", dettagliSegnalazioneViewController.getPrimaryStage());
		});

		dettagliSegnalazioneViewController.button1.setOnAction(event -> {
			ViewLoader.caricaView("EffettuaSegnalazioneView.fxml",
					dettagliSegnalazioneViewController.getPrimaryStage());
		});

		dettagliSegnalazioneViewController.button2.setOnAction(event -> {
			ViewLoader.caricaView("RiscattaRicompensaView.fxml", dettagliSegnalazioneViewController.getPrimaryStage());
		});

		dettagliSegnalazioneViewController.button1.setText("NUOVA\nSEGNALAZIONE");
		dettagliSegnalazioneViewController.button2.setText("RISCATTA\nRICOMPENSA");
		dettagliSegnalazioneViewController.line.setLayoutX(645.0);
	}
}
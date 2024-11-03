package boundary;

public class SegnalazioniAssegnateViewState implements State {

	@Override
	public void configureView(DettagliSegnalazioneViewController dettagliSegnalazioneViewController) {

		System.out.println("Configurazione DettagliSegnalazione per SegnalazioniAssegnateView");

		dettagliSegnalazioneViewController.indietroButton.setOnAction(event -> {

			ViewLoader.caricaView("SegnalazioniAssegnateView.fxml",
					dettagliSegnalazioneViewController.getPrimaryStage());
		});

		dettagliSegnalazioneViewController.button2.setDisable(true);
		dettagliSegnalazioneViewController.button2.setVisible(false);

		dettagliSegnalazioneViewController.button1.setLayoutX(537.375);
		dettagliSegnalazioneViewController.line.setLayoutX(537.375);
		dettagliSegnalazioneViewController.button1.setText("VISUALIZZA\nASSEGNAZIONI");

		dettagliSegnalazioneViewController.button1.setOnAction(event -> {
			ViewLoader.caricaView("SegnalazioniAssegnateView.fxml",
					dettagliSegnalazioneViewController.getPrimaryStage());
		});

	}

}

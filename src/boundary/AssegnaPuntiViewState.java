package boundary;

public class AssegnaPuntiViewState implements State {

	@Override
	public void configureView(DettagliSegnalazioneViewController dettagliSegnalazioneViewController) {

		System.out.println("Configurazione DettagliSegnalazione per AssegnaPuntiView");

		dettagliSegnalazioneViewController.indietroButton.setOnAction(event -> {
			ViewLoader.caricaView("AssegnaPuntiView.fxml", dettagliSegnalazioneViewController.getPrimaryStage());
		});

		dettagliSegnalazioneViewController.button1.setOnAction(event -> {
			ViewLoader.caricaView("GestisciSegnalazioniView.fxml",
					dettagliSegnalazioneViewController.getPrimaryStage());
		});

		dettagliSegnalazioneViewController.button2.setOnAction(event -> {
			ViewLoader.caricaView("AssegnaPuntiView.fxml", dettagliSegnalazioneViewController.getPrimaryStage());

		});

		dettagliSegnalazioneViewController.button1.setText("GESTISCI\nSEGNALAZIONI");
		dettagliSegnalazioneViewController.button2.setText("ASSEGNA\nPUNTI");

		dettagliSegnalazioneViewController.line.setLayoutX(645.0);

	}
}

package logic.boundary;

import java.util.List;
import java.util.Optional;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import logic.beans.SegnalazioneBean;
import logic.controller.AssegnaPuntiController;
import logic.observer.Observer;

public class AssegnaPuntiViewController implements Observer{

	@FXML
	private Button exitButton;
	@FXML
	private Button gestisciSegnalazioniButton;
	@FXML
	private Button dettagliButton;
	@FXML
	private Button assegnaButton;
	@FXML
	private TextField puntiTextField;

	@FXML
	private TableView<SegnalazioneBean> segnalazioniTable;
	@FXML
	private TableColumn<SegnalazioneBean, Integer> idColumn;
	@FXML
	private TableColumn<SegnalazioneBean, String> descrizioneColumn;
	@FXML
	private TableColumn<SegnalazioneBean, String> posizioneColumn;

	@FXML
	private TableColumn<SegnalazioneBean, String> operatoreColumn;
	

	private static AssegnaPuntiViewController instance;
	private AssegnaPuntiController assegnaPuntiController = AssegnaPuntiController.getInstance();
	private DettagliSegnalazioneViewController dettagliSegnalazioneViewController = DettagliSegnalazioneViewController.getInstance();
    private boolean osservatoreRegistrato = false; 
	
	
	@FXML
	public void initialize() {
		String valoreInizialeTextField = puntiTextField.getText();
        caricaSegnalazioniRisolte();    
        configuraColonneTabella();
        configuraPulsanti();
        impostaListenerSelezione(valoreInizialeTextField);
        
        // Verifica se l'osservatore è già stato registrato
        if (!osservatoreRegistrato) {
        	assegnaPuntiController.registraOsservatoreSegnalazioniRisolte(this);
            osservatoreRegistrato = true;  // Segna l'osservatore come registrato
        }
	}
	
	
    private void configuraColonneTabella() {
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdSegnalazione()).asObject());
        descrizioneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescrizione()));
        posizioneColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPosizione()));
    }

	private void configuraPulsanti() {	
		dettagliButton.setDisable(true);
		dettagliButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.DETTAGLI_VIEW));
        assegnaButton.setOnAction(event -> assegnaPuntiSegnalazione());
		exitButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.LOGIN_VIEW));
		gestisciSegnalazioniButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.GESTISCI_SEGNALAZIONI_VIEW));
	}
	private void assegnaPuntiSegnalazione() {
		SegnalazioneBean segnalazioneSelezionata = segnalazioniTable.getSelectionModel().getSelectedItem();
		String inputTextField = puntiTextField.getText();

		if (inputTextField.isEmpty()) {
			showAlert("Errore", "Il campo non può essere vuoto.");
			return;
		}

		try {

			int punti = Integer.parseInt(inputTextField);
			segnalazioneSelezionata.setPuntiAssegnati(punti);
		} catch (NumberFormatException e) {

			showAlert("Errore", "Inserisci un numero intero valido.");
		}

		if (segnalazioneSelezionata != null) {

			boolean successo = assegnaPuntiController.assegnaPunti(segnalazioneSelezionata);

			if (!successo) {
				showAlert("Errore Assegnazione", "Si è verificato un errore durante l'assegnazione dei punti.");
			}
		} else {
			showAlert("Selezione Mancante", "Devi selezionare una segnalazione.");
		}
	}
	
    private void impostaListenerSelezione(String valoreInizialeTextField) {
        segnalazioniTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
				dettagliButton.setDisable(false);
				
                dettagliSegnalazioneViewController.setSegnalazioneBean(newValue);
                dettagliSegnalazioneViewController.setCallerType(CallerType.CONTROLLER1);
            } else {
				dettagliButton.setDisable(true);
				puntiTextField.setText(valoreInizialeTextField);
            }
        });
    }


	private void caricaSegnalazioniRisolte() {
		List<SegnalazioneBean> segnalazioniRisolte = assegnaPuntiController.getSegnalazioniDaRiscontrate();

		ObservableList<SegnalazioneBean> segnalazioni = FXCollections.observableArrayList(segnalazioniRisolte);
		segnalazioniTable.setItems(segnalazioni);
	}

	private Optional<ButtonType> showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);

		return alert.showAndWait();
	}



	public static AssegnaPuntiViewController getInstance() {
		if (instance == null) {
			instance = new AssegnaPuntiViewController();
		}
		return instance;
	}


	@Override
	public void update() {
		List<SegnalazioneBean> segnalazioniRisolte = assegnaPuntiController.getSegnalazioniRisolte();

		ObservableList<SegnalazioneBean> segnalazioni = FXCollections.observableArrayList(segnalazioniRisolte);
		segnalazioniTable.setItems(segnalazioni);
	}

}

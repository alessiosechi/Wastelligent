package boundary;

import java.util.List;

import controller.LoginController;
import controller.RiscattaRicompensaController;
import exceptions.DailyRedemptionLimitException;
import exceptions.InsufficientPointsException;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

import javafx.scene.control.TableColumn;
import javafx.scene.control.Label;

import javafx.stage.Stage;

import model.domain.RicompensaBean;

import model.domain.UtenteBean;

public class RiscattaRicompensaViewController {

	@FXML
	private TableView<RicompensaBean> tableViewRicompense;

	// colonne della TableView
	@FXML
	private TableColumn<RicompensaBean, String> colNomeRicompensa;
	@FXML
	private TableColumn<RicompensaBean, String> colCodiceRiscatto;
	@FXML
	private TableColumn<RicompensaBean, String> colDescrizione;
	@FXML
	private TableColumn<RicompensaBean, Integer> colValore;
	@FXML
	private TableColumn<RicompensaBean, String> colDataRiscatto;
	@FXML
	private TableColumn<RicompensaBean, String> colDataScadenza;
	@FXML
	private TableColumn<RicompensaBean, Integer> colPuntiUtilizzati;
	
	@FXML
	private Label dettagliRicompensaLabel;
	@FXML
	private Label labelSaldoPunti;
	@FXML
	private ComboBox<String> comboBoxRicompense;
	@FXML
	private Button btnRiscatta;
	@FXML
	private Button exitButton;
	@FXML
	private Button nuovaSegnalazioneButton;
	@FXML
	private Button visualizzaStoricoButton;
	
	
	private static RiscattaRicompensaViewController instance;
	private Stage primaryStage;
	private RiscattaRicompensaController riscattaRicompensaController = RiscattaRicompensaController.getInstance();
	private LoginController loginController = LoginController.getInstance();
    private int idUtente;
    private List<RicompensaBean> listaRicompenseAPI;
	
	

	@FXML
	public void initialize() {
		UtenteBean utente = loginController.getUtente();
		idUtente = utente.getIdUtente();

		caricaPuntiUtente();
		mostraRicompenseDisponibili();
		setupEventHandlers();
		configuraColonneTableView();

		tableViewRicompense.setItems(FXCollections.observableArrayList(getRicompenseRiscattate()));
	}
	
	private void caricaPuntiUtente() {
		// idUtente è un intero, un tipo primitivo, non utilizzo Bean
		int puntiUtente = riscattaRicompensaController.ottieniPuntiUtente(idUtente);
		labelSaldoPunti.setText(String.valueOf(puntiUtente));
		
	}
	
	private void mostraRicompenseDisponibili() {
        // carico le ricompense disponibili solo una volta
        if (listaRicompenseAPI == null) {
        	listaRicompenseAPI = riscattaRicompensaController.ottieniRicompenseAPI();
        }
		ObservableList<String> ricompense = FXCollections.observableArrayList();
		listaRicompenseAPI.forEach(r -> ricompense.add(r.getNome()));
		comboBoxRicompense.setItems(ricompense);
	}

	private void setupEventHandlers() {
		nuovaSegnalazioneButton.setOnAction(event -> ViewLoader.caricaView("EffettuaSegnalazioneView.fxml", primaryStage));
		exitButton.setOnAction(event -> ViewLoader.caricaView("LoginView.fxml", primaryStage));
		visualizzaStoricoButton.setOnAction(event -> ViewLoader.caricaView("StoricoView.fxml", primaryStage));
		btnRiscatta.setOnAction(event -> riscattaSelezione());

		
		
        comboBoxRicompense.setOnAction(event -> {
            int selectedIndex = comboBoxRicompense.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                RicompensaBean ricompensaSelezionata = listaRicompenseAPI.get(selectedIndex);
                mostraDettagliRicompensa(ricompensaSelezionata); // mostro i dettagli nella label
            }
        });
	}

	private void configuraColonneTableView() {
//		colNomeRicompensa.setCellValueFactory(new PropertyValueFactory<>("nome"));
//		colCodiceRiscatto.setCellValueFactory(new PropertyValueFactory<>("codiceRiscatto"));
//		colDescrizione.setCellValueFactory(new PropertyValueFactory<>("descrizione"));
//		colValore.setCellValueFactory(new PropertyValueFactory<>("valore"));
//		colDataRiscatto.setCellValueFactory(new PropertyValueFactory<>("dataRiscatto"));
//		colDataScadenza.setCellValueFactory(new PropertyValueFactory<>("dataScadenza"));
//		colPuntiUtilizzati.setCellValueFactory(new PropertyValueFactory<>("punti"));
		
		
		/*
		 *  la soluzione con sopra presenta un accoppiamento forte con i nomi degli attributi della classe Ricompensabean, 
		 *  invece, la soluzione sotto fa riferimento direttamente ai metodi getter di RicompensaBean, in tal modo, qualsiasi 
		 *  modifica del nome delle proprietà all'interno di RicompensaBean non richiederà modifiche alla configurazione delle 
		 *  colonne della TableView
		 */
		
		colNomeRicompensa.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));

		colCodiceRiscatto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCodiceRiscatto()));

		colDescrizione.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescrizione()));

		colValore.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getValore()).asObject());

		colDataRiscatto.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataRiscatto()));

		colDataScadenza.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataScadenza()));

		colPuntiUtilizzati.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPunti()).asObject());
		
		
	}



	private List<RicompensaBean> getRicompenseRiscattate() {
		return riscattaRicompensaController.ottieniRicompenseUtente(idUtente);
	}

	private void mostraDettagliRicompensa(RicompensaBean ricompensaBean) {
		int puntiNecessari = riscattaRicompensaController.calcolaPuntiNecessari(ricompensaBean.getValore());
		String dettagli = String.format(
		        "Nome: %s%nValore: %d€%nDescrizione: %s%nData scadenza: %s%nPunti necessari: %d",
		        ricompensaBean.getNome(), 
		        ricompensaBean.getValore(), 
		        ricompensaBean.getDescrizione(),
		        ricompensaBean.getDataScadenza(), 
		        puntiNecessari
		);
		dettagliRicompensaLabel.setText(dettagli);

	}

	private void riscattaSelezione() {
	    int selectedIndex = comboBoxRicompense.getSelectionModel().getSelectedIndex();
	    if (selectedIndex >= 0) {
	        
	        RicompensaBean ricompensaBean = listaRicompenseAPI.get(selectedIndex);
	        ricompensaBean.setIdUtente(idUtente);

	        try {
	            boolean result = riscattaRicompensaController.riscatta(ricompensaBean);

	            String message = result ? "Ricompensa riscattata con successo!" 
	                                    : "Impossibile riscattare la ricompensa. Riprova.";
	            showAlert(result ? "Successo" : "Errore", message);

	            if (result) {
	                // se il riscatto ha avuto successo, aggiorno la pagina
	                ViewLoader.caricaView("RiscattaRicompensaView.fxml", primaryStage);
	            }

	        } catch (DailyRedemptionLimitException e) {

	            showAlert("Limite riscatti giornalieri raggiunto", e.getMessage());

	        } catch (InsufficientPointsException e) {

	            showAlert("Punti insufficienti", e.getMessage());

	        } catch (Exception e) {
	            showAlert("Errore", "Si è verificato un errore imprevisto. Riprova più tardi.");
	            e.printStackTrace();
	        }
	        
	    } else {
	        showAlert("Nessuna Ricompensa Selezionata", "Seleziona una ricompensa prima di riscattarla.");
	    }
	}




	private void showAlert(String titolo, String messaggio) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(titolo);
		alert.setHeaderText(null);
		alert.setContentText(messaggio);
		alert.showAndWait();
	}
	

	
	// Singleton: restituisco l'istanza di RiscattaRicompensaViewController
	public static RiscattaRicompensaViewController getInstance() {
		if (instance == null) {
			instance = new RiscattaRicompensaViewController();
		}
		return instance;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	


}

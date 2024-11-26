package logic.boundary;

import java.util.List;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import logic.controller.RiscattaRicompensaController;
import logic.exceptions.ConnessioneAPIException;
import logic.exceptions.DailyRedemptionLimitException;
import logic.exceptions.GestioneRiscattoException;
import logic.exceptions.InsufficientPointsException;
import logic.model.domain.RicompensaBean;
import logic.observer.Observer;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Label;

public class RiscattaRicompensaViewController implements Observer{

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
	private RiscattaRicompensaController riscattaRicompensaController = RiscattaRicompensaController.getInstance();
    private List<RicompensaBean> listaRicompenseAPI;
    private boolean osservatoreRegistrato = false; 
	

	@FXML
	public void initialize() {

		caricaPuntiUtente();
		mostraRicompenseDisponibili();
		setupEventHandlers();
		configuraColonneTableView();

		tableViewRicompense.setItems(FXCollections.observableArrayList(getRicompenseRiscattate()));
		
        if (!osservatoreRegistrato) {
        	riscattaRicompensaController.registraOsservatoreRicompenseRiscattate(this);
            osservatoreRegistrato = true;  
        }
	}
	
	private void caricaPuntiUtente() {
		// idUtente è un intero, un tipo primitivo, non utilizzo Bean
		int puntiUtente = riscattaRicompensaController.ottieniPuntiUtente();
		labelSaldoPunti.setText(String.valueOf(puntiUtente));	
	}
	
	private void mostraRicompenseDisponibili() {
        // carico le ricompense disponibili solo una volta
        if (listaRicompenseAPI == null) {
        	try {
				listaRicompenseAPI = riscattaRicompensaController.ottieniRicompenseAPI();
			} catch (ConnessioneAPIException e) {
	            showAlert("Errore di connessione", e.getMessage());
			}
        }
        
		ObservableList<String> ricompense = FXCollections.observableArrayList();
		listaRicompenseAPI.forEach(r -> ricompense.add(r.getNome()));
		comboBoxRicompense.setItems(ricompense);
	}

	private void setupEventHandlers() {
		nuovaSegnalazioneButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.EFFETTUA_SEGNALAZIONE_VIEW));
		exitButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.LOGIN_VIEW));
		visualizzaStoricoButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.STORICO_VIEW));
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
	
		/*
		 * la soluzione sotto fa riferimento direttamente ai metodi getter di RicompensaBean, in tal modo, qualsiasi
		 * modifica del nome delle proprietà all'interno di RicompensaBean non richiederà modifiche alla configurazione delle
		 * colonne della TableView
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
		return riscattaRicompensaController.ottieniRicompenseUtente();
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
	    
	    if (selectedIndex < 0) {
	        showAlert("Nessuna Ricompensa Selezionata", "Seleziona una ricompensa.");
	        return;
	    }
	    
        RicompensaBean ricompensaBean = listaRicompenseAPI.get(selectedIndex);


        try {
            boolean result = riscattaRicompensaController.riscatta(ricompensaBean);

            String message = result ? "Ricompensa riscattata con successo!" 
                                    : "Impossibile riscattare la ricompensa. Riprova.";
            showAlert(result ? "Successo" : "Errore", message);


        } catch (DailyRedemptionLimitException e) {

            showAlert("Limite riscatti giornalieri raggiunto", e.getMessage());

        } catch (InsufficientPointsException e) {

            showAlert("Punti insufficienti", e.getMessage());

        } catch (GestioneRiscattoException e) {

            showAlert("Errore nel recupero del codice di riscatto", e.getMessage());

        }catch (Exception e) {
            showAlert("Errore", "Si è verificato un errore imprevisto. Riprova più tardi.");
        }
	}




	private void showAlert(String titolo, String messaggio) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(titolo);
		alert.setHeaderText(null);
		alert.setContentText(messaggio);
		alert.showAndWait();
	}
	
	public static RiscattaRicompensaViewController getInstance() {
		if (instance == null) {
			instance = new RiscattaRicompensaViewController();
		}
		return instance;
	}

	@Override
	public void update() {
		tableViewRicompense.setItems(FXCollections.observableArrayList(riscattaRicompensaController.getRicompenseRiscattate()));
		labelSaldoPunti.setText(String.valueOf(riscattaRicompensaController.ottieniPuntiUtente()));
	}

}

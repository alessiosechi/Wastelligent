package logic.boundary2;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import logic.beans.RicompensaBean;
import logic.beans.RiscattoBean;
import logic.boundary.ViewInfo;
import logic.boundary.ViewLoader;
import logic.controller.RiscattaRicompensaController;
import logic.exceptions.ConnessioneAPIException;
import logic.exceptions.DailyRedemptionLimitException;
import logic.exceptions.InsufficientPointsException;
import logic.exceptions.GestioneRiscattoException;
import logic.observer.Observer;

import java.util.List;

public class EffettuaRiscattoViewController implements Observer {
    @FXML
    private BorderPane rootPane;
    @FXML
    private Label saldoPuntiLabel;
    @FXML
    private ChoiceBox<String> choiceBoxRicompense;  
    @FXML
    private Button riscattaButton;  
    @FXML
    private Button storicoButton;  
    @FXML
    private ListView<String> listViewRiscatti;  
    @FXML
    private TextArea dettagliRicompensaTextArea;   
    @FXML
    private TextArea dettagliRiscattoTextArea;

    private RiscattaRicompensaController riscattaRicompensaController = RiscattaRicompensaController.getInstance();
    private List<RicompensaBean> listaRicompenseAPI;

    @FXML
    public void initialize() {
        AnchorPane sidebar = SidebarLoader.loadSidebar(SidebarType.UTENTE_BASE_SIDEBAR);
        rootPane.setLeft(sidebar);
        
        
        riscattaRicompensaController.caricaUtente();
        riscattaRicompensaController.registraOsservatoreRiscatti(this);
        
        

        caricaPuntiUtente();
        mostraRicompenseDisponibili();
        caricaRiscatti(); 
        setupEventHandlers();
    }

    private void caricaPuntiUtente() {
        int puntiUtente = riscattaRicompensaController.ottieniPuntiUtente();
        saldoPuntiLabel.setText(String.valueOf(puntiUtente));
    }

    private void mostraRicompenseDisponibili() {
        try {
            listaRicompenseAPI = riscattaRicompensaController.ottieniRicompenseAPI();
        } catch (ConnessioneAPIException e) {
            showAlert("Errore di connessione", e.getMessage());
        }

        ObservableList<String> ricompense = FXCollections.observableArrayList();
        for (RicompensaBean ricompensa : listaRicompenseAPI) {
            ricompense.add(ricompensa.getNome());
        }
        choiceBoxRicompense.setItems(ricompense);
    }
    private void caricaRiscatti() {
        List<RiscattoBean> riscatti = riscattaRicompensaController.getRiscatti();

        ObservableList<String> riscattoStrings = FXCollections.observableArrayList();
        for (RiscattoBean riscatto : riscatti) {
            riscattoStrings.add(riscatto.getNomeRicompensa() + " - " + riscatto.getDataRiscatto());
        }

        listViewRiscatti.setItems(riscattoStrings);
    }

    private void setupEventHandlers() {
        dettagliRicompensaTextArea.setEditable(false);
        dettagliRiscattoTextArea.setEditable(false);
        riscattaButton.setOnAction(this::riscattaSelezione);
        storicoButton.setOnAction(event -> ViewLoader.caricaView(ViewInfo.STORICO_PUNTI_VIEW));

        choiceBoxRicompense.setOnAction(event -> {
            int selectedIndex = choiceBoxRicompense.getSelectionModel().getSelectedIndex();
            if (selectedIndex >= 0) {
                RicompensaBean ricompensaSelezionata = listaRicompenseAPI.get(selectedIndex);
                mostraDettagliRicompensa(ricompensaSelezionata);
            }
        });
        
        listViewRiscatti.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                RiscattoBean selezionato = riscattaRicompensaController.getRiscatti().get(listViewRiscatti.getSelectionModel().getSelectedIndex());
                
                String dettagli = String.format(
                        "Nome ricompensa: %s%nCodice Riscatto: %s%nPunti: %d%nData riscatto: %s%nValore: %d€%nDescrizione: %s%nData scadenza: %s", 
                        selezionato.getNomeRicompensa(), selezionato.getCodiceRiscatto(), selezionato.getPunti(), 
                        selezionato.getDataRiscatto(), selezionato.getValoreRicompensa(), selezionato.getDescrizioneRicompensa(), 
                        selezionato.getDataScadenzaRicompensa());

                dettagliRiscattoTextArea.setText(dettagli);
            }
        });
        
    }

    private void mostraDettagliRicompensa(RicompensaBean ricompensaBean) {
        int puntiNecessari = riscattaRicompensaController.calcolaPuntiNecessari(ricompensaBean.getValore());
        String dettagli = String.format(
                "Nome: %s%nValore: %d€%nDescrizione: %s%nData scadenza: %s%nPunti necessari: %d",
                ricompensaBean.getNome(), ricompensaBean.getValore(), ricompensaBean.getDescrizione(),
                ricompensaBean.getDataScadenza(), puntiNecessari);
        dettagliRicompensaTextArea.setText(dettagli);
    }

    private void riscattaSelezione(ActionEvent event) {
        int selectedIndex = choiceBoxRicompense.getSelectionModel().getSelectedIndex();

        if (selectedIndex < 0) {
            showAlert("Nessuna Ricompensa Selezionata", "Seleziona una ricompensa.");
            return;
        }

        RicompensaBean ricompensaBean = listaRicompenseAPI.get(selectedIndex);

        try {
            boolean result = riscattaRicompensaController.riscatta(ricompensaBean);

            String message = result ? "Ricompensa riscattata con successo!" : "Impossibile riscattare la ricompensa. Riprova.";
            showAlert(result ? "Successo" : "Errore", message);

        } catch (DailyRedemptionLimitException e) {
            showAlert("Limite riscatti giornalieri raggiunto", e.getMessage());
        } catch (InsufficientPointsException e) {
            showAlert("Punti insufficienti", e.getMessage());
        } catch (GestioneRiscattoException e) {
            showAlert("Errore nel recupero del codice di riscatto", e.getMessage());
        } catch (Exception e) {
            showAlert("Errore", "Si è verificato un errore imprevisto. Riprova più tardi.");
        }
    }

    // Mostra gli alert
    private void showAlert(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }


    @Override
    public void update() {
        ObservableList<String> riscattoStrings = FXCollections.observableArrayList();
        for (RiscattoBean riscatto : riscattaRicompensaController.getRiscatti()) {
            riscattoStrings.add(riscatto.getNomeRicompensa() + " - " + riscatto.getDataRiscatto());
        }
        listViewRiscatti.setItems(riscattoStrings);
        saldoPuntiLabel.setText(String.valueOf(riscattaRicompensaController.ottieniPuntiUtente()));
    }
}

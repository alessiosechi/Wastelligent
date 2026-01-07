# Wastelligent

[![SonarCloud](https://sonarcloud.io/api/project_badges/measure?project=alessiosechi_Wastelligent&metric=alert_status)](https://sonarcloud.io/project/overview?id=alessiosechi_Wastelligent)

Wastelligent è un sistema progettato per consentire ai cittadini di segnalare rifiuti abbandonati e discariche illegali, monitorando e coordinando la loro rimozione. Il sistema premia attivamente la partecipazione, incentivando il coinvolgimento nella tutela dell'ambiente e nella prevenzione dell'inquinamento.

## Tipologie di utenti

Il sistema prevede tre tipi di utenti:

- **Utente base**: può segnalare la presenza di rifiuti abbandonati e discariche illegali, indicando la posizione, una descrizione del problema e, se possibile, una foto.
  
- **Esperto ecologico**: riceve le segnalazioni inviate dagli utenti base e le assegna agli operatori ecologici disponibili per la rimozione dei rifiuti.

- **Operatore ecologico**: dopo aver ricevuto l'assegnazione di una segnalazione, interviene per la rimozione dei rifiuti e contrassegna la segnalazione come completata al termine dell'intervento.

## Programma di ricompense

Per incentivare la partecipazione degli utenti, il sistema include un programma di ricompense. Gli utenti base che contribuiscono attivamente possono accumulare punti, che potranno essere utilizzati per riscattare ricompense. I punti vengono assegnati dagli esperti ecologici in base alla posizione e all'entità del problema risolto.

---

## Istruzioni per l'installazione

### Prerequisiti

- **JavaFX**: Per eseguire il progetto, è necessario scaricare JavaFX dal [sito ufficiale](https://openjfx.io/) e configurarlo correttamente nell'ambiente di sviluppo (Eclipse, IntelliJ, o un altro IDE).

### Configurazione

1. **Configurazione JavaFX**:
   - Seguire le istruzioni per configurare JavaFX all'interno dell'IDE.
   - Specificare i seguenti **VM arguments**:
     ```
     --module-path "<percorso-javafx>/lib" --add-modules javafx.controls,javafx.fxml,javafx.web
     ```

2. **Clonare la repository**:
   - Clonare questo repository nella propria macchina locale:
     ```bash
     git clone https://github.com/alessiosechi/Wastelligent.git
     ```

3. **Importare il progetto**.

4. **Configurare le dipendenze**:
   - All'interno della cartella del progetto, è presente una cartella chiamata **libs**. Questa cartella contiene tutti i file `.jar` necessari per il progetto.
   - Aggiungere manualmente tutti i file `.jar` nella cartella **libs** come dipendenze.

6. **Creazione del database [opzionale]**:
     - Eseguire lo script SQL presente nella cartella DB attraverso il software HeidiSQL per creare le tabelle necessarie.
     - Modificare il file `resources/db.properties` per configurare la connessione al proprio database MySQL. 
   
---

### Esecuzione dell'applicazione

**Se non è stato creato il database**, avviare l'applicazione e scegliere di **utilizzare la versione demo**. Altrimenti, se il database è stato creato, scegliere la versione che si desidera utilizzare.   
   


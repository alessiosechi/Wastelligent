# Wastelligent

Wastelligent è un sistema progettato per consentire ai cittadini di segnalare rifiuti abbandonati e discariche illegali, monitorando e coordinando la loro rimozione. Il sistema premia attivamente la partecipazione, incentivando il coinvolgimento nella cura dell'ambiente e nella prevenzione dell'inquinamento.

## Tipologie di utenti

Il sistema prevede tre tipi di utenti:

- **Utente base**: Può segnalare la presenza di rifiuti abbandonati e discariche illegali, indicando la posizione, una descrizione del problema e, se possibile, una foto. Può anche partecipare attivamente alla risoluzione della segnalazione.
  
- **Esperto ecologico**: Riceve le segnalazioni degli utenti base e le assegna agli operatori ecologici disponibili per la rimozione dei rifiuti.

- **Operatore ecologico**: Dopo aver ricevuto l'assegnazione di una segnalazione, l'operatore ecologico interviene per la rimozione dei rifiuti e segna la segnalazione come completata una volta risolto il problema.

## Programma di ricompense

Per incentivare la partecipazione degli utenti, il sistema include un programma di ricompense. Gli utenti base che contribuiscono attivamente possono accumulare punti, che potranno essere utilizzati per riscattare ricompense. I punti vengono assegnati dagli esperti ecologici in base alla posizione e all'entità del problema risolto.

---

## Istruzioni per l'installazione

### Prerequisiti

- **JavaFX**: Per eseguire il progetto, è necessario scaricare JavaFX dal [sito ufficiale](https://openjfx.io/) e configurarlo correttamente nell'ambiente di sviluppo (Eclipse, IntelliJ, o un altro IDE).

### Configurazione dell'ambiente

1. **Configurazione JavaFX**:
   - Visitare il sito ufficiale di JavaFX e scaricare l'ultima versione.
   - Seguire le istruzioni per configurare JavaFX all'interno dell'IDE.

2. **Clonare la repository**:
   - Clonare questo repository nella propria macchina locale:
     ```bash
     git clone https://github.com/tuo-username/Wastelligent.git
     ```
     
3. **Importare il progetto**:
   - Dopo aver clonato il progetto, aprire l'IDE (Eclipse o un altro).
   - Importare il progetto selezionando **File** > **Import** > **Existing Projects into Workspace**.
   - Selezionare la cartella del progetto clonato.

4. **Configurare le dipendenze**:
   - All'interno della cartella del progetto, è presente una cartella chiamata **libs**. Questa cartella contiene tutti i file `.jar` necessari per il progetto.
   - Aggiungere manualmente tutti i file `.jar` nella cartella **libs** come dipendenze.

6. **Creazione del database [opzionale]**:
     - Eseguire lo script SQL presente nella cartella DB attraverso il software HeidiSQL per creare le tabelle necessarie.
     - Modificare il file `resources/db.properties` per configurare la connessione al proprio database MySQL. 
   
### Esecuzione dell'applicazione

Se non è stato creato il database, avviare l'applicazione e scegliere di **utilizzare la versione demo**. Altrimenti, se il database è stato creato, scegliere la versione che si desidera utilizzare.
   
   


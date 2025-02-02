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

- **JavaFX**: Per eseguire il progetto, è necessario scaricare JavaFX dal [sito ufficiale](https://openjfx.io/) e configurarlo correttamente nel tuo ambiente di sviluppo (Eclipse, IntelliJ, o un altro IDE).

### Configurazione dell'ambiente

1. **Scarica e configura JavaFX**:
   - Visita il sito ufficiale di JavaFX e scarica l'ultima versione.
   - Segui le istruzioni per configurare JavaFX all'interno del tuo IDE (ad esempio, in Eclipse puoi aggiungere JavaFX nelle impostazioni di progetto).

2. **Clonare la repository**:
   - Clona questo repository nella tua macchina locale:
     ```bash
     git clone https://github.com/tuo-username/Wastelligent.git
     ```

3. **Importare il progetto in Eclipse**:
   - Dopo aver clonato il progetto, apri Eclipse (o un altro IDE a tua scelta).
   - Seleziona "Importa" > "Progetto esistente" > "Progetto Java".
   - Seleziona la cartella del progetto che hai clonato.

4. **Configurare le dipendenze**:
   - All'interno della cartella del progetto, troverai una cartella chiamata **Lib**. Questa cartella contiene tutti i file `.jar` necessari per il progetto.
   - Aggiungi tutti i file `.jar` nella cartella **Lib** come dipendenze nel tuo progetto, affinché il progetto possa essere eseguito correttamente.

5. **Cartelle del progetto**:
   - **SRC**: Contiene il codice sorgente del progetto.
   - **Test**: Contiene le classi di test scritte con JUnit, utilizzate per il testing delle funzionalità. All'interno della cartella **Test** troverai i test per le principali funzionalità del sistema, come la gestione delle segnalazioni, l'assegnazione delle ricompense e il flusso di lavoro tra utenti base, esperti ecologici e operatori ecologici. I test sono organizzati per garantire la correttezza del codice e la stabilità del sistema.

6. **Creazione del database**:
   - **Opzione 1: Configurazione del database (per l'esecuzione completa)**:
     - Prima di eseguire l'applicazione, è necessario creare le tabelle nel tuo database MySQL.
     - Assicurati di avere MySQL configurato correttamente e crea le tabelle utilizzando gli script SQL forniti (se presenti nel progetto) o seguendo le specifiche del database nel codice sorgente.
   
   - **Opzione 2: Utilizzare la versione demo**:
     - Se non desideri configurare il database, puoi utilizzare una **versione demo** dell'applicazione. In questa versione, il sistema è preconfigurato con un set di dati di esempio e non richiede una connessione a un database MySQL. Questa modalità ti permette di testare le funzionalità principali senza la necessità di configurare il database.
   
### Esecuzione dell'applicazione

1. Avvia l'applicazione dal tuo IDE o eseguendo il comando appropriato nel terminale:
   ```bash
   java -jar Wastelligent.jar

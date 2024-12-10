package logic.model.dao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import logic.model.domain.Ricompensa;
import logic.model.domain.Riscatto;

public class RiscattoDaoFileSystem implements RiscattoDao {

    private static final Logger logger = Logger.getLogger(RiscattoDaoFileSystem.class.getName());
    private static final String FILE_PATH = "resources/riscatti.csv";



    @Override
    public void registra(Riscatto riscatto) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {

            writer.write(riscattoToCsv(riscatto));
            writer.newLine(); // aggiungo una nuova riga per il prossimo riscatto
            logger.info("Riscatto registrato con successo.");
        } catch (IOException e) {
            logger.severe("Errore durante il salvataggio del riscatto: " + e.getMessage());
        }
    }

    @Override
    public List<Riscatto> getRiscattiByUtente(int idUtente) {
        List<Riscatto> riscatti = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Riscatto riscatto = csvToRiscatto(line);
                if (riscatto != null && riscatto.getIdUtente() == idUtente) {
                    riscatti.add(riscatto);
                }
            }
        } catch (IOException e) {
            logger.severe("Errore durante il recupero dei riscatti: " + e.getMessage());
        }

        return riscatti;
    }

    private String riscattoToCsv(Riscatto riscatto) {
        return riscatto.getNomeRicompensa() + ","
                + riscatto.getDescrizioneRicompensa() + ","
                + riscatto.getValoreRicompensa() + ","
                + riscatto.getDataScadenzaRicompensa() + ","
                + riscatto.getCodiceRiscatto() + ","
                + riscatto.getDataRiscatto() + ","
                + riscatto.getPunti() + ","
                + riscatto.getIdUtente();
    }


    private Riscatto csvToRiscatto(String csvLine) {
        String[] fields = csvLine.split(",");
        if (fields.length != 8) {
            return null; // se la riga non contiene il numero corretto di campi, ignoriamola
        }

        String nome = fields[0];
        String descrizione = fields[1];
        int valore = Integer.parseInt(fields[2]);
        String dataScadenza = fields[3];
        String codiceRiscatto = fields[4];
        String dataRiscatto = fields[5];
        int puntiUtilizzati = Integer.parseInt(fields[6]);
        int idUtente = Integer.parseInt(fields[7]);

        Ricompensa ricompensa = new Ricompensa(nome, valore, descrizione, dataScadenza);
        return new Riscatto(ricompensa, idUtente, puntiUtilizzati, codiceRiscatto, dataRiscatto);
    }
    
    
    
}


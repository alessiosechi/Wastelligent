package logic.model.dao;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import logic.model.domain.Riscatto;

public class RiscattoDaoSerializable implements RiscattoDao {
    private static final String FILE_PATH = "resources/riscatti.ser";
    private static final Logger logger = Logger.getLogger(RiscattoDaoSerializable.class.getName());

    @Override
    public void registra(Riscatto riscatto) {
        List<Riscatto> riscatti = caricaRiscatti();
        riscatti.add(riscatto); 
        salvaRiscatti(riscatti); 
    }

    @Override
    public List<Riscatto> getRiscattiByUtente(int idUtente) {
        List<Riscatto> riscattiUtente = new ArrayList<>();
        List<Riscatto> riscatti = caricaRiscatti();
        
        for (Riscatto r : riscatti) {
            if (r.getIdUtente() == idUtente) {
                riscattiUtente.add(r);
            }
        }
        
        return riscattiUtente;
    }

    @SuppressWarnings("unchecked")
	private List<Riscatto> caricaRiscatti() {
        List<Riscatto> riscatti = new ArrayList<>();
        try (FileInputStream fileIn = new FileInputStream(FILE_PATH);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            riscatti = (List<Riscatto>) in.readObject();
        } catch (FileNotFoundException e) {
            logger.warning("File non trovato.");
        } catch (IOException | ClassNotFoundException e) {
            logger.severe("Errore durante il caricamento dei riscatti: " + e.getMessage());
        }
        return riscatti;
    }

    // salva la lista aggiornata nel file
    private void salvaRiscatti(List<Riscatto> riscatti) {
        try (FileOutputStream fileOut = new FileOutputStream(FILE_PATH);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(riscatti);
            logger.info("Lista di riscatti salvata nel file: " + FILE_PATH);
        } catch (IOException e) {
            logger.severe("Errore durante il salvataggio dei riscatti nel file: " + e.getMessage());
        }
    }
}


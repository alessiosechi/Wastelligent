package logic.model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import logic.model.dao.queries.SegnalazioneQueries;
import logic.model.domain.Segnalazione;


public class SegnalazioneDaoDatabase implements SegnalazioneDao {

    private static volatile SegnalazioneDaoDatabase instance;
    private static final Logger logger = Logger.getLogger(SegnalazioneDaoDatabase.class.getName());
    public static final String COL_ID_SEGNALAZIONE = "id_segnalazione";
    public static final String COL_ID_UTENTE = "id_utente";
    public static final String COL_DESCRIZIONE = "descrizione";
    public static final String COL_FOTO = "foto";
    public static final String COL_STATO = "stato";
    public static final String COL_LATITUDINE = "latitudine";
    public static final String COL_LONGITUDINE = "longitudine";
    public static final String COL_PUNTI = "punti_assegnati";

    private SegnalazioneDaoDatabase() {
    }

    public static SegnalazioneDaoDatabase getInstance() {
        SegnalazioneDaoDatabase result = instance;
        if (instance == null) {
            synchronized (SegnalazioneDaoDatabase.class) {
                result = instance;
                if (result == null) {
                    instance = result = new SegnalazioneDaoDatabase();
                }
            }
        }
        return result;
    }

    @Override
    public void salvaSegnalazione(Segnalazione segnalazione) {
        Connection connessione = null;

        try {
            connessione = DBConnection.getConnection();

            int righeInserite = SegnalazioneQueries.inserisciSegnalazione(connessione, segnalazione);
            if (righeInserite > 0) {
                connessione.commit();
            } else {
                connessione.rollback();
            }

        } catch (SQLException e) {
            logger.severe("Errore durante il salvataggio della segnalazione: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Errore inaspettato durante il salvataggio della segnalazione: " + e.getMessage());
        } 
    }



    @Override
    public void eliminaSegnalazione(Segnalazione segnalazione) {
        Connection connessione = null;

        try {
            connessione = DBConnection.getConnection();

            int righeEliminate = SegnalazioneQueries.eliminaSegnalazione(connessione, segnalazione);
            if (righeEliminate > 0) {
                connessione.commit();
                logger.info("Segnalazione eliminata con successo");
            } else {
                connessione.rollback();
            }
        } catch (SQLException e) {
            logger.severe("Errore durante l'eliminazione della segnalazione: " + e.getMessage());
        }
    }
    @Override
    public void aggiornaStato(int idSegnalazione, String stato) {
        Connection connessione = null;

        try {
            connessione = DBConnection.getConnection();

            int righeAggiornate = SegnalazioneQueries.aggiornaStato(connessione, idSegnalazione, stato);
            if (righeAggiornate > 0) {
                connessione.commit();
            } else {
                connessione.rollback();
            }

        } catch (SQLException e) {
            logger.severe("Errore durante l'aggiornamento dello stato della segnalazione: " + e.getMessage());
        }
    }

	
    @Override
    public List<Segnalazione> getSegnalazioniByStato(String stato) {
        Connection connessione = null;
        List<Segnalazione> segnalazioni = new ArrayList<>();

        try {
            connessione = DBConnection.getConnection();
            ResultSet resultSet = SegnalazioneQueries.getSegnalazioniByStato(connessione, stato);
            while (resultSet.next()) {
                Segnalazione segnalazione = new Segnalazione();

                segnalazione.setIdSegnalazione(resultSet.getInt(COL_ID_SEGNALAZIONE));
                segnalazione.setIdUtente(resultSet.getInt(COL_ID_UTENTE));
                segnalazione.setDescrizione(resultSet.getString(COL_DESCRIZIONE));
                segnalazione.setFoto(resultSet.getString(COL_FOTO)); 
                segnalazione.setStato(resultSet.getString(COL_STATO));
                segnalazione.setLatitudine(resultSet.getDouble(COL_LATITUDINE));
                segnalazione.setLongitudine(resultSet.getDouble(COL_LONGITUDINE));

                segnalazioni.add(segnalazione);
            }

        } catch (SQLException e) {
            logger.severe("Errore durante il recupero delle segnalazioni con stato " + stato + ": " + e.getMessage());
        }

        return segnalazioni;
    }

	
    @Override
    public List<Segnalazione> getSegnalazioniAssegnate(int idOperatore) {
        Connection connessione = null;
        List<Segnalazione> segnalazioni = new ArrayList<>();

        try {
            connessione = DBConnection.getConnection();

            ResultSet resultSet = SegnalazioneQueries.getSegnalazioniAssegnate(connessione, idOperatore);

            while (resultSet.next()) {
                Segnalazione segnalazione = new Segnalazione();
                segnalazione.setIdSegnalazione(resultSet.getInt(COL_ID_SEGNALAZIONE));
                segnalazione.setIdUtente(resultSet.getInt(COL_ID_UTENTE));
                segnalazione.setDescrizione(resultSet.getString(COL_DESCRIZIONE));
                segnalazione.setFoto(resultSet.getString(COL_FOTO)); 
                segnalazione.setStato(resultSet.getString(COL_STATO));
                segnalazione.setLatitudine(resultSet.getDouble(COL_LATITUDINE));
                segnalazione.setLongitudine(resultSet.getDouble(COL_LONGITUDINE));

                segnalazioni.add(segnalazione);
            }

        } catch (SQLException e) {
            logger.severe("Errore durante il recupero delle segnalazioni assegnate all'operatore " + idOperatore + ": " + e.getMessage());
        }

        return segnalazioni;
    }


    
    @Override
    public List<Segnalazione> getSegnalazioniRiscontrate(int idUtente) {
        Connection connessione = null;
        List<Segnalazione> segnalazioni = new ArrayList<>();

        try {
            connessione = DBConnection.getConnection();

            ResultSet resultSet = SegnalazioneQueries.getSegnalazioniRiscontrate(connessione, idUtente);

            while (resultSet.next()) {
                Segnalazione segnalazione = new Segnalazione();
                segnalazione.setIdSegnalazione(resultSet.getInt(COL_ID_SEGNALAZIONE));
                segnalazione.setIdUtente(resultSet.getInt(COL_ID_UTENTE));
                segnalazione.setDescrizione(resultSet.getString(COL_DESCRIZIONE));
                segnalazione.setFoto(resultSet.getString(COL_FOTO)); 
                segnalazione.setStato(resultSet.getString(COL_STATO));
                segnalazione.setLatitudine(resultSet.getDouble(COL_LATITUDINE));
                segnalazione.setLongitudine(resultSet.getDouble(COL_LONGITUDINE));
                segnalazione.setPuntiAssegnati(resultSet.getInt(COL_PUNTI));

                segnalazioni.add(segnalazione);
            }

        } catch (SQLException e) {
            logger.severe("Errore durante il recupero delle segnalazioni riscontrate per l'utente " + idUtente + ": " + e.getMessage());
        }

        return segnalazioni;
    }
    
    
    
    

    @Override
    public void assegnaOperatore(int idSegnalazione, int idOperatore) {
        Connection connessione = null;

        try {
            connessione = DBConnection.getConnection();
            connessione.setAutoCommit(false);

            int righeInserite = SegnalazioneQueries.inserisciAssegnazione(connessione, idSegnalazione, idOperatore);
            if (righeInserite > 0) {
                connessione.commit();
            } else {
                connessione.rollback();
            }

        } catch (SQLException e) {
            logger.severe("Errore durante l'assegnazione dell'operatore: " + e.getMessage());
        }
    }


    @Override
    public void assegnaPunti(int idSegnalazione, int punti) {
        Connection connessione = null;

        try {
            connessione = DBConnection.getConnection();
            connessione.setAutoCommit(false);

            int righeInserite = SegnalazioneQueries.assegnaPuntiSegnalazione(connessione, idSegnalazione, punti);
            if (righeInserite > 0) {
                connessione.commit();
            } else {
                connessione.rollback();
            }

        } catch (SQLException e) {
            logger.severe("Errore durante l'assegnazione dei punti: " + e.getMessage());
        }
    }

}

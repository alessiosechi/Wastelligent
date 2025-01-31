package logic.model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import logic.model.dao.queries.RiscattoQueries;
import logic.model.domain.Ricompensa;
import logic.model.domain.Riscatto;

public class RiscattoDaoDatabase implements RiscattoDao {

    private static final Logger logger = Logger.getLogger(RiscattoDaoDatabase.class.getName());

    @Override
    public void registra(Riscatto riscatto) {
        Connection connessione = null;
        Statement stmt = null;

        try {
            connessione = DBConnection.getConnection();
            stmt = connessione.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);


            int righeInserite = RiscattoQueries.inserisciRiscatto(stmt, riscatto);
            if (righeInserite > 0) {
                connessione.commit();
            } else {
                connessione.rollback();
            }
        } catch (SQLException e) {
            logger.severe("Errore durante l'inserimento del riscatto: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                logger.severe("Errore durante la chiusura del Statement: " + e.getMessage());
            }
        }
    }

    @Override
    public List<Riscatto> getRiscattiByUtente(int idUtente) {
        List<Riscatto> riscatti = new ArrayList<>();
        Statement stmt = null;

        try {
            Connection connessione = DBConnection.getConnection();
            stmt = connessione.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = RiscattoQueries.getRiscattiByUtente(stmt, idUtente);

            while (resultSet.next()) {
                String nome = resultSet.getString("nome");
                String descrizione = resultSet.getString("descrizione");
                int valore = resultSet.getInt("valore");
                String dataScadenza = resultSet.getDate("data_scadenza").toString();
                String codiceRiscatto = resultSet.getString("codice_riscatto");
                String dataRiscatto = resultSet.getDate("data_riscatto").toString();
                int puntiUtilizzati = resultSet.getInt("punti_utilizzati");
                
                Ricompensa ricompensa = new Ricompensa(nome, valore, descrizione, dataScadenza);
                Riscatto riscatto = new Riscatto(ricompensa, idUtente, puntiUtilizzati, codiceRiscatto, dataRiscatto);

                riscatti.add(riscatto);
            }
        } catch (SQLException e) {
            logger.severe("Errore durante il recupero dei riscatti dell'utente: " + e.getMessage());
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                logger.severe("Errore durante la chiusura del Statement: " + e.getMessage());
            }
        }

        return riscatti;
    }
}


package logic.model.dao.queries;


import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import logic.model.domain.Riscatto;

public class RiscattoQueries {
	private RiscattoQueries() {}

    public static int inserisciRiscatto(Statement stmt, Riscatto riscatto) throws SQLException {
        String insertStatement = String.format("INSERT INTO riscatti (id_utente, nome, descrizione, codice_riscatto, valore, data_riscatto, data_scadenza, punti_utilizzati) "
                + "VALUES (%d, '%s', '%s', '%s', %d, '%s', '%s', %d)",
                riscatto.getIdUtente(), riscatto.getNomeRicompensa(), riscatto.getDescrizioneRicompensa(), riscatto.getCodiceRiscatto(),
                riscatto.getValoreRicompensa(), riscatto.getDataRiscatto(), riscatto.getDataScadenzaRicompensa(), riscatto.getPunti());

        return stmt.executeUpdate(insertStatement);
    }

    public static ResultSet getRiscattiByUtente(Statement stmt, int idUtente) throws SQLException {
        String selectStatement = String.format("SELECT nome, descrizione, codice_riscatto, valore, data_riscatto, data_scadenza, punti_utilizzati "
                + "FROM riscatti WHERE id_utente = %d", idUtente);

        return stmt.executeQuery(selectStatement);
    }
}


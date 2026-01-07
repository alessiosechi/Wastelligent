package logic.model.dao.queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import logic.model.domain.Riscatto;

public class RiscattoQueries {

    private RiscattoQueries() {
    }

    public static int inserisciRiscatto(Connection conn, Riscatto riscatto) throws SQLException {
        String insertStatement = "INSERT INTO riscatti "
                + "(id_utente, nome, descrizione, codice_riscatto, valore, data_riscatto, data_scadenza, punti_utilizzati) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(insertStatement)) {
            stmt.setInt(1, riscatto.getIdUtente());
            stmt.setString(2, riscatto.getNomeRicompensa());
            stmt.setString(3, riscatto.getDescrizioneRicompensa());
            stmt.setString(4, riscatto.getCodiceRiscatto());
            stmt.setInt(5, riscatto.getValoreRicompensa());
            stmt.setString(6, riscatto.getDataRiscatto());
            stmt.setString(7, riscatto.getDataScadenzaRicompensa());
            stmt.setInt(8, riscatto.getPunti());

            return stmt.executeUpdate();
        }
    }

    public static ResultSet getRiscattiByUtente(Connection conn, int idUtente) throws SQLException {
        String selectStatement = "SELECT nome, descrizione, codice_riscatto, valore, data_riscatto, data_scadenza, punti_utilizzati "
                + "FROM riscatti WHERE id_utente = ?";

        try (PreparedStatement stmt = conn.prepareStatement(selectStatement)) {
            stmt.setInt(1, idUtente);
            return stmt.executeQuery();
        }
    }
}

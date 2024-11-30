package logic.model.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import logic.model.domain.OperatoreEcologico;
import logic.model.domain.Ruolo;

public class OperatoreEcologicoDaoDatabase implements OperatoreEcologicoDao{
    private static volatile OperatoreEcologicoDaoDatabase instance;
    private static final Logger logger = Logger.getLogger(OperatoreEcologicoDaoDatabase.class.getName());

    public static OperatoreEcologicoDaoDatabase getInstance() {
    	OperatoreEcologicoDaoDatabase result = instance;

        if (result == null) {
            synchronized (OperatoreEcologicoDaoDatabase.class) {
                result = instance;
                if (result == null) {
                    instance = result = new OperatoreEcologicoDaoDatabase();
                }
            }
        }
        return result;
    }

    @Override
    public List<OperatoreEcologico> estraiOperatoriEcologici() {
        List<OperatoreEcologico> operatoriEcologici = new ArrayList<>();
        
        String sql = "SELECT id_utente, username FROM utenti WHERE tipo_utente = ?";

        try (PreparedStatement stmt = DBConnection.getConnection().prepareStatement(sql)) {

            stmt.setInt(1, Ruolo.OPERATORE_ECOLOGICO.getId());

            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    int idUtente = resultSet.getInt("id_utente");
                    String username = resultSet.getString("username");

                    OperatoreEcologico operatore = new OperatoreEcologico(idUtente, username);
                    operatoriEcologici.add(operatore);
                }
            }
        } catch (SQLException e) {
            logger.severe("Errore durante il recupero delle informazioni degli operatori ecologici. Dettagli: " + e.getMessage());
        }

        return operatoriEcologici;
    }

}

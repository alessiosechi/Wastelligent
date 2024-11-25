package logic.model.dao;

import java.util.ArrayList;
import java.util.List;

import logic.model.domain.Ricompensa;

public class RicompensaDaoInMemory implements RicompensaDao {

    private final List<Ricompensa> ricompense = new ArrayList<>();


    @Override
    public void registraRicompensaRiscattata(Ricompensa ricompensa) {
        if (ricompensa == null) {
            throw new IllegalArgumentException("La ricompensa non può essere null.");
        }
        ricompense.add(ricompensa);
    }

    @Override
    public List<Ricompensa> getRicompenseByUtente(int idUtente) {
        List<Ricompensa> risultato = new ArrayList<>();
        for (Ricompensa r : ricompense) {
            if (r.getIdUtente() == idUtente) {
                risultato.add(r);
            }
        }
        return risultato;
    }
}

package logic.model.dao;

import java.util.ArrayList;
import java.util.List;

import logic.model.domain.Riscatto;

public class RiscattoDaoInMemory implements RiscattoDao {

    private final List<Riscatto> riscatti = new ArrayList<>();

    @Override
    public void registra(Riscatto riscatto) {
        riscatti.add(riscatto);
    }

    @Override
    public List<Riscatto> getRiscattiByUtente(int idUtente) {
        List<Riscatto> riscattiUtente = new ArrayList<>();
        for (Riscatto r : riscatti) {
            if (r.getIdUtente() == idUtente) {
            	riscattiUtente.add(r);
            }
        }
        return riscattiUtente;
    }
}


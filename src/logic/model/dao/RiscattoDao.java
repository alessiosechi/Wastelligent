package logic.model.dao;

import java.util.List;

import logic.model.domain.Riscatto;

public interface RiscattoDao {
    void registra(Riscatto riscatto);

    List<Riscatto> getRiscattiByUtente(int idUtente);
}


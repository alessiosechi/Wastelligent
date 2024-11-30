package logic.model.dao;

import java.util.List;

import logic.model.domain.ListaOperatoriEcologici;
import logic.model.domain.OperatoreEcologico;

public class OperatoreEcologicoDaoInMemory implements OperatoreEcologicoDao {

    @Override
    public List<OperatoreEcologico> estraiOperatoriEcologici() {
        return ListaOperatoriEcologici.getInstance().getOperatoriEcologici();
    }

}

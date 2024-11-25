package logic.model.dao;

import java.util.List;

import logic.model.domain.Ricompensa;

public interface RicompensaDao {
	void registraRicompensaRiscattata(Ricompensa ricompensa);
    List<Ricompensa> getRicompenseByUtente(int idUtente);
}

package model.dao;

import java.util.List;

import model.domain.Ricompensa;

public interface RicompensaDao {
	void registraRicompensaRiscattata(Ricompensa ricompensa);
    List<Ricompensa> getRicompenseByUtente(int idUtente);
}

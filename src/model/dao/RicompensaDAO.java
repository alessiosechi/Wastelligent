package model.dao;

import java.util.List;

import model.domain.Ricompensa;

public interface RicompensaDAO {
	void registraRicompensaRiscattata(Ricompensa ricompensa);
    List<Ricompensa> getRicompenseByUtente(int idUtente);
}

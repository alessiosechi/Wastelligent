package logic.model.dao;

import java.util.List;

import logic.exceptions.CodiceRiscattoNonTrovatoException;
import logic.exceptions.ConnessioneAPIException;
import logic.model.domain.Ricompensa;

public interface RicompensaDao {
	List<Ricompensa> getRicompense()throws ConnessioneAPIException;
	String getCodiceRiscatto(int idRicompensa)throws ConnessioneAPIException, CodiceRiscattoNonTrovatoException;
}

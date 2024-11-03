package model.dao;

import model.domain.Ricompensa;
import java.util.List;

public interface ListaRicompenseGithubDAO {
	List<Ricompensa> getRicompense();
	String getCodiceRiscatto(int idRicompensa);

}

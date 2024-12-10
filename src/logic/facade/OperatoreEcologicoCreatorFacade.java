package logic.facade;

import java.util.List;

import logic.model.dao.DaoFactory;
import logic.model.dao.SegnalazioneDao;
import logic.model.domain.Segnalazione;
import logic.model.domain.OperatoreEcologico;

public class OperatoreEcologicoCreatorFacade {
	private static SegnalazioneDao segnalazioneDao= DaoFactory.getDao(SegnalazioneDao.class);

	private OperatoreEcologicoCreatorFacade() {

	}

	public static OperatoreEcologico caricaOperatore(int idUtente, String username) {
		List<Segnalazione> segnalazioniAssegnate = segnalazioneDao.getSegnalazioniAssegnate(idUtente);
		return new OperatoreEcologico(idUtente, username, segnalazioniAssegnate);
	}
	
	
	
}

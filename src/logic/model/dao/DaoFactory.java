package logic.model.dao;

import java.util.HashMap;
import java.util.Map;

import logic.config.PersistenceConfigurator;
import logic.config.PersistenceProvider;

public class DaoFactory {

	// mappa per gestire i DAO
	private static Map<Class<?>, Object> daoMap = new HashMap<>();

	private DaoFactory() {
	}

	@SuppressWarnings("unchecked")
	public static <T> T getDao(Class<T> daoClass) {

		// se il DAO non è già stato creato, lo inizializzo
		if (!daoMap.containsKey(daoClass)) {
			T dao = createDao(daoClass);
			daoMap.put(daoClass, dao);
		}
		return (T) daoMap.get(daoClass);
	}

	// metodo per creare il DAO specifico in base al tipo
	@SuppressWarnings("unchecked")
	private static <T> T createDao(Class<T> daoClass) {
		if (daoClass == UtenteDao.class) {
			return (T) createUtenteDao();
		} else if (daoClass == SegnalazioneDao.class) {
			return (T) createSegnalazioneDao();
		} else if (daoClass == UtenteBaseDao.class) {
			return (T) createUtenteBaseDao();
		} else if (daoClass == RiscattoDao.class) {
			return (T) createRiscattoDao();
		} else if (daoClass == RicompensaDao.class) {
			return (T) createListaRicompenseDao();
		} else if (daoClass == CoordinateDao.class) {
			return (T) createCoordinateDao();
		}

		return null;
	}

	// metodi specifici per creare i DAO
	private static UtenteDao createUtenteDao() {
		return isInMemory() ? (UtenteDao) new UtenteDaoInMemory() : (UtenteDao) new UtenteDaoDatabase();
	}

	private static SegnalazioneDao createSegnalazioneDao() {
		return isInMemory() ? (SegnalazioneDao) new SegnalazioneDaoInMemory()
				: (SegnalazioneDao) new SegnalazioneDaoDatabase();
	}

	private static UtenteBaseDao createUtenteBaseDao() {
		return isInMemory() ? (UtenteBaseDao) new UtenteBaseDaoInMemory() : (UtenteBaseDao) new UtenteBaseDaoDatabase();
	}

	private static RiscattoDao createRiscattoDao() {
		return isInMemory() ? (RiscattoDao) new RiscattoDaoInMemory() : (RiscattoDao) new RiscattoDaoDatabase();
	}

	private static RicompensaDao createListaRicompenseDao() {
		return new RicompensaDaoAPI();
	}

	private static CoordinateDao createCoordinateDao() {
		return new CoordinateDaoAPI();
	}

	// verifico se la persistenza è in memoria o su database
	private static boolean isInMemory() {
		return PersistenceConfigurator.getCurrentProvider() == PersistenceProvider.IN_MEMORY;
	}
}

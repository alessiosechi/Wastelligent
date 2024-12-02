package logic.model.dao;

import java.util.HashMap;
import java.util.Map;

import logic.config.PersistenceConfigurator;
import logic.config.PersistenceProvider;

public class DaoFactory {

    // mappa per gestire i DAO
    private static Map<Class<?>, Object> daoMap = new HashMap<>();
    
    
    
    private DaoFactory() {
    	// costruttore vuoto
    }
    
    @SuppressWarnings("unchecked")
	public static <T> T getDao(Class<T> daoClass) {
        // Se il DAO non è già stato creato, lo inizializza
        if (!daoMap.containsKey(daoClass)) {
            T dao = createDao(daoClass);
            daoMap.put(daoClass, dao);
        }
        return (T) daoMap.get(daoClass);
    }

    // metodo per creare il DAO specifico in base al tipo
    @SuppressWarnings("unchecked")
	private static <T> T createDao(Class<T> daoClass) {
    	if (daoClass == AccountDao.class) {
            return (T) createLoginDAO();
        } else if (daoClass == SegnalazioneDao.class) {
            return (T) createSegnalazioneDAO();
        } else if (daoClass == UtenteBaseDao.class) {
            return (T) createUtenteDAO();
        } else if (daoClass == OperatoreEcologicoDao.class) {
            return (T) createOperatoreEcologicoDAO();
        } else if (daoClass == RicompensaDao.class) {
            return (T) createRicompensaDAO();
        } else if (daoClass == ListaRicompenseDao.class) {
        	return (T) createListaRicompenseDAO();
        }
        return null;
    }

    // metodi specifici per creare i DAO
    private static AccountDao createLoginDAO() {
        return isInMemory() ? (AccountDao) new AccountDaoInMemory() : (AccountDao) AccountDaoDatabase.getInstance();
    }
    
    private static SegnalazioneDao createSegnalazioneDAO() {
        return isInMemory() ? (SegnalazioneDao) new SegnalazioneDaoInMemory() : (SegnalazioneDao) SegnalazioneDaoDatabase.getInstance();
    }

    private static UtenteBaseDao createUtenteDAO() {
        return isInMemory() ? (UtenteBaseDao) new UtenteBaseDaoInMemory() : (UtenteBaseDao) UtenteBaseDaoDatabase.getInstance();
    }
    private static OperatoreEcologicoDao createOperatoreEcologicoDAO() {
        return isInMemory() ? (OperatoreEcologicoDao) new OperatoreEcologicoDaoInMemory() : (OperatoreEcologicoDao) OperatoreEcologicoDaoDatabase.getInstance();
    }

    private static RicompensaDao createRicompensaDAO() {
        return isInMemory() ? (RicompensaDao) new RicompensaDaoInMemory() : (RicompensaDao) RicompensaDaoDatabase.getInstance();
    }
    private static ListaRicompenseDao createListaRicompenseDAO() {
    	return ListaRicompenseDaoGithub.getInstance();
    }


    // verifico se la persistenza è in memoria o su database
    private static boolean isInMemory() {
        return PersistenceConfigurator.getCurrentProvider() == PersistenceProvider.IN_MEMORY;
    }
}


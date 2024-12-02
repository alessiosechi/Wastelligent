package logic.controller;

import java.util.logging.Logger;

import logic.model.dao.DaoFactory;
import logic.beans.CredenzialiBean;
import logic.config.PersistenceConfigurator;
import logic.config.PersistenceProvider;
import logic.model.dao.AccountDao;
import logic.model.domain.Ruolo;
import logic.model.domain.Utente;
import logic.model.domain.UtenteCorrente;

public class LoginController { 
	private static volatile LoginController instance;
	private static Utente utente = null;
	private static AccountDao accountDao;
	private static UtenteFactory utenteFactory = new UtenteFactory();
	private static UtenteCorrente utenteCorrente=UtenteCorrente.getInstance();

	private static final Logger logger = Logger.getLogger(LoginController.class.getName());

	private LoginController() {
	}

	public static LoginController getInstance() {
		LoginController result = instance;

		if (instance == null) {
			// blocco sincronizzato
			synchronized (LoginController.class) {
				result = instance;
				if (result == null) {
					instance = result = new LoginController();
						
					try {
						accountDao = DaoFactory.getDao(AccountDao.class);

					} catch (Exception e) {
						logger.severe("Errore durante l'inizializzazione del DAO: " + e.getMessage());
					}
				}
			}
		}

		return result;
	}

	public int effettuaLogin(CredenzialiBean credenzialiBean) {
		try {
			String username = credenzialiBean.getUsername();
			String password = credenzialiBean.getPassword();
			

			int ruoloId = accountDao.autenticazione(username, password);
			int idUtente = accountDao.getIdByUsername(username);

			setUtente(idUtente, username, Ruolo.fromInt(ruoloId));
			return 1;

		} catch (Exception e) {
			logger.severe("Errore durante la fase di login.");
			return -1;
		}

	}



	private static void setUtente(int idUtente, String username, Ruolo ruolo) {
		if (ruolo == null) {
			throw new IllegalArgumentException("Ruolo non può essere null");
		}
		utente = utenteFactory.createUtente(idUtente, username, ruolo);	
		utenteCorrente.setUtente(utente);
	}
	
	
	public void configurePersistence(boolean useDemoVersion) {
		PersistenceProvider provider = useDemoVersion ? PersistenceProvider.IN_MEMORY : PersistenceProvider.DATABASE;
		PersistenceConfigurator.configurePersistence(provider);
	}

	public String ottieniView(int interfacciaSelezionata) { // restituisce la view iniziale da caricare
		return utente.getViewIniziale(interfacciaSelezionata);
	}


	public static void logout() { // il logout potrebbe non servire, tanto ad ogni login sovrascrivo l'utente
		utente = null;
		logger.info("Logout effettuato correttamente.");
	}
}

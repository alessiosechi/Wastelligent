package logic.controller;

import java.util.logging.Logger;

import logic.beans.CredenzialiBean;
import logic.exceptions.RegistrazioneUtenteException;
import logic.exceptions.UsernameAlreadyTakenException;
import logic.model.dao.AccountDao;
import logic.model.dao.DaoFactory;

public class RegistrazioneController { // OK
	private static volatile RegistrazioneController instance;
	private static AccountDao accountDao;

	private static final Logger logger = Logger.getLogger(RegistrazioneController.class.getName());

	private RegistrazioneController() {
	}

	public static RegistrazioneController getInstance() {
		RegistrazioneController result = instance;

		if (instance == null) {
			synchronized (RegistrazioneController.class) {
				result = instance;
				if (result == null) {
					instance = result = new RegistrazioneController();
						
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
	
	public void registraUtente(CredenzialiBean credenzialiBean) throws UsernameAlreadyTakenException, RegistrazioneUtenteException {
		// controllo se lo username è già stato preso
		if (accountDao.isUsernameTaken(credenzialiBean.getUsername())) {
			throw new UsernameAlreadyTakenException("Il nome utente è già in uso. Scegline un altro.");
		}
		boolean success=accountDao.registraUtente(credenzialiBean.getUsername(), credenzialiBean.getPassword());
	    if (!success) {
	        throw new RegistrazioneUtenteException("Errore durante la registrazione dell'utente.");
	    }

	}
}

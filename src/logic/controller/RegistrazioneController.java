package logic.controller;

import java.util.logging.Logger;

import logic.beans.CredenzialiBean;
import logic.exceptions.RegistrazioneUtenteException;
import logic.exceptions.UsernameAlreadyTakenException;
import logic.model.dao.DaoFactory;
import logic.model.dao.UtenteDao;

public class RegistrazioneController { 
	private static volatile RegistrazioneController instance;
	private UtenteDao utenteDao;

	private static final Logger logger = Logger.getLogger(RegistrazioneController.class.getName());

	private RegistrazioneController() {
		try {
			utenteDao = DaoFactory.getDao(UtenteDao.class);
		} catch (Exception e) {
			logger.severe("Errore durante l'inizializzazione del DAO: " + e.getMessage());
		}
	}

	public static RegistrazioneController getInstance() {
		RegistrazioneController result = instance;
		if (instance == null) {
			synchronized (RegistrazioneController.class) {
				result = instance;
				if (result == null) {
					instance = result = new RegistrazioneController();
				}
			}
		}

		return result;
	}
	
	public void registraUtente(CredenzialiBean credenzialiBean) throws UsernameAlreadyTakenException, RegistrazioneUtenteException {
		// controllo se lo username è già stato preso
		if (utenteDao.isUsernameTaken(credenzialiBean.getUsername())) {
			throw new UsernameAlreadyTakenException("Il nome utente è già in uso. Scegline un altro.");
		}
		boolean success=utenteDao.registraUtente(credenzialiBean.getUsername(), credenzialiBean.getPassword());
	    if (!success) {
	        throw new RegistrazioneUtenteException("Errore durante la registrazione dell'utente.");
	    }

	}
}

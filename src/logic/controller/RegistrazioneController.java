package logic.controller;

import java.util.logging.Logger;

import logic.beans.SignUpBean;
import logic.exceptions.RegistrazioneUtenteException;
import logic.exceptions.UsernameAlreadyTakenException;
import logic.model.dao.DaoFactory;
import logic.model.dao.UtenteDao;
import logic.model.domain.Ruolo;

public class RegistrazioneController {
	private static final Logger logger = Logger.getLogger(RegistrazioneController.class.getName());
	private UtenteDao utenteDao;

	public RegistrazioneController() {
		try {
			utenteDao = DaoFactory.getDao(UtenteDao.class);
		} catch (Exception e) {
			logger.severe("Errore durante l'inizializzazione del DAO: " + e.getMessage());
		}
	}
	
	public void registraUtente(SignUpBean signUpBean) throws UsernameAlreadyTakenException, RegistrazioneUtenteException {
		// controllo se lo username è già stato preso
		if (utenteDao.isUsernameTaken(signUpBean.getUsername())) {
			throw new UsernameAlreadyTakenException("Il nome utente è già in uso. Scegline un altro.");
		}
		boolean success=utenteDao.registraUtente(signUpBean.getUsername(), signUpBean.getPassword(), Ruolo.fromInt(signUpBean.getTipologiaId()));
	    if (!success) {
	        throw new RegistrazioneUtenteException("Errore durante la registrazione dell'utente.");
	    }

	}
}

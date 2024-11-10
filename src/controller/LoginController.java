package controller;

import java.util.logging.Logger;

import exceptions.RegistrazioneUtenteException;
import exceptions.UsernameAlreadyTakenException;
import model.dao.LoginDAO;
import model.dao.LoginDAOImplementazione;
import model.domain.CredenzialiBean;
import model.domain.Utente;
import model.domain.UtenteCorrente;
import model.domain.Ruolo;

public class LoginController {
	private static volatile LoginController instance;
	private static Utente utente = null;
	private static LoginDAO loginDAO;
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
						// inizializzazione di LoginDAO
						loginDAO = LoginDAOImplementazione.getInstance();

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


			int ruoloId = loginDAO.autenticazione(username, password);
			int idUtente = loginDAO.getIdByUsername(username);

			setUtente(idUtente, username, Ruolo.fromInt(ruoloId));

			return 1;

		} catch (Exception e) {
			e.printStackTrace();
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

	public String ottieniView(int interfacciaSelezionata) { // restituisce la view iniziale da caricare
		return utente.getViewIniziale(interfacciaSelezionata);
	}

	
	public void registraUtente(CredenzialiBean credenzialiBean) throws UsernameAlreadyTakenException, RegistrazioneUtenteException {
		// controllo se lo username è già stato preso
		if (loginDAO.isUsernameTaken(credenzialiBean.getUsername())) {
			throw new UsernameAlreadyTakenException("Il nome utente è già in uso. Scegline un altro.");
		}
		boolean success=loginDAO.registraUtente(credenzialiBean.getUsername(), credenzialiBean.getPassword());
	    if (!success) {
	        throw new RegistrazioneUtenteException("Errore durante la registrazione dell'utente.");
	    }

	}
	
	public static void logout() { // il logout potrebbe non servire, tanto ad ogni login sovrascrivo l'utente
		utente = null;
		logger.info("Logout effettuato correttamente.");
	}
}

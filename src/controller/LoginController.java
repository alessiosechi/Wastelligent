package controller;

import java.util.logging.Logger;

import exceptions.UsernameAlreadyTakenException;
import model.dao.LoginDAO;
import model.dao.LoginDAOImplementazione;
import model.domain.CredenzialiBean;
import model.domain.Utente;
import model.domain.UtenteBean;
import model.domain.Ruolo;

public class LoginController {
	private static volatile LoginController instance;
	private static Utente utente = null;
	private static LoginDAO loginDAO;
	private static UtenteFactory utenteFactory = new UtenteFactory();

	private static final Logger logger = Logger.getLogger(LoginController.class.getName());
	private static final String REGISTRAZIONE_ERRORE_MSG = "Errore durante la registrazione.";

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
						logger.severe("Errore durante l'inizializzazione di LoginDAO: " + e.getMessage());
						throw new RuntimeException("Impossibile inizializzare LoginDAO");
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

			logger.info(String.format("USERNAME: %s", username));

			logger.info(String.format("PASSWORD: ****%s", password.substring(password.length() - 2)));

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

	public void registraUtente(CredenzialiBean credenzialiBean) throws UsernameAlreadyTakenException {
		try {

			// controllo se lo username è già stato preso
			if (loginDAO.isUsernameTaken(credenzialiBean.getUsername())) {
				throw new UsernameAlreadyTakenException();
			}

			int success = loginDAO.registraUtente(credenzialiBean.getUsername(), credenzialiBean.getPassword());

			if (success != 1) {
				throw new RuntimeException(REGISTRAZIONE_ERRORE_MSG);
			}

		} catch (UsernameAlreadyTakenException e) {
			logger.warning("Il nome utente è già in uso: " + credenzialiBean.getUsername());
			throw e; // rilancio l'eccezione che ho appena catturato, la passo al livello superiore

		} catch (Exception e) {
			logger.severe(REGISTRAZIONE_ERRORE_MSG + e.getMessage());
			throw new RuntimeException(REGISTRAZIONE_ERRORE_MSG, e);
		}

	}

	private static void setUtente(int idUtente, String username, Ruolo ruolo) {
		if (ruolo == null) {
			throw new IllegalArgumentException("Ruolo non può essere null");
		}
		// stampaDettagliUtenteCorrente();
		utente = utenteFactory.createUtente(idUtente, username, ruolo);
	}

	public String ottieniView(int interfacciaSelezionata) { // restituisce la view iniziale da caricare
		return utente.getViewIniziale(interfacciaSelezionata);
	}

	public UtenteBean getUtente() {

		return convertUtenteToBean(utente);
	}

	public static void logout() { // il logout potrebbe non servire, tanto ad ogni login sovrascrivo l'utente
		utente = null;
		logger.info("Logout effettuato correttamente.");
	}

//	public void stampaDettagliUtenteCorrente() {
//	    if (utente != null) {
//	        System.out.println("Ultimo utente: " + utente.getUsername());
//	        
//	        if (utente instanceof UtenteBase) {
//	            UtenteBase utenteBase = (UtenteBase) utente;
//	            System.out.println("Dettagli Utente Base: " + utenteBase.getIdUtente());
//	        } else if (utente instanceof EspertoEcologico) {
//	            EspertoEcologico esperto = (EspertoEcologico) utente;
//	            System.out.println("Dettagli Esperto Ecologico: " + esperto.getIdUtente());
//	        } else if (utente instanceof OperatoreEcologico) {
//	            OperatoreEcologico operatore = (OperatoreEcologico) utente;
//	            System.out.println("Dettagli Operatore Ecologico: " + operatore.getIdUtente());
//	        }
//	    } else {
//	        System.out.println("Nessun utente è attualmente autenticato.");
//	    }
//	}

	public UtenteBean convertUtenteToBean(Utente utente) {
		UtenteBean utenteBean = new UtenteBean();
		utenteBean.setIdUtente(utente.getIdUtente());
		utenteBean.setUsername(utente.getUsername());
		return utenteBean;
	}
}

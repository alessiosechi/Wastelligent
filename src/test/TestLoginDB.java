package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeEach;
import logic.model.dao.UtenteDao;
import logic.model.dao.UtenteDaoDatabase;

public class TestLoginDB {

	private UtenteDao utenteDao;

	@BeforeEach
	public void setup() {
		utenteDao = new UtenteDaoDatabase();
	}

	@Test
	public void testAutenticazioneSuccesso() {
		String username = "utente_base1";
		String password = "password1";

		// test con credenziali valide
		boolean isAutenticato = utenteDao.autenticazione(username, password);
		assertTrue(isAutenticato, "Il login dovrebbe andare a buon fine.");
	}

	@Test
	public void testAutenticazioneFallitaCredenzialiErrate() {
		String username = "testUser";
		String password = "wrongPassword";

		// test con credenziali errate
		boolean isAutenticato = utenteDao.autenticazione(username, password);
		assertFalse(isAutenticato, "Il login non dovrebbe avvenire, le credenziali sono errate.");
	}

}

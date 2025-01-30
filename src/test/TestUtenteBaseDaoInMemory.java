package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

import logic.model.dao.UtenteBaseDao;
import logic.model.dao.UtenteBaseDaoInMemory;

public class TestUtenteBaseDaoInMemory {

	private UtenteBaseDao dao;

	@BeforeEach
	public void setUp() {
		dao = new UtenteBaseDaoInMemory();
	}

	@Test
	public void testAggiungiPunti() {
		dao.aggiungiPunti(1, 10);
		int punti = dao.estraiPunti(1);
		assertEquals(10, punti);
	}

	@Test
	public void testSottrarrePunti() {
		dao.aggiungiPunti(1, 20);
		dao.sottraiPunti(1, 5);
		int punti = dao.estraiPunti(1);
		assertEquals(15, punti);
	}

}

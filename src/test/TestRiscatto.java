package test;

import logic.model.domain.Ricompensa;
import logic.model.domain.Riscatto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestRiscatto {

	private static final String NOME_RICOMPENSA = "Buono Amazon";
	private static final String DESCRIZIONE_RICOMPENSA = "Buono da 10 Euro su Amazon";
	private static final String DATA_SCADENZA_RICOMPENSA = "2025-12-31";
	private static final int VALORE_RICOMPENSA = 10;
	private static final int ID_UTENTE = 1;
	private static final int PUNTI = 500;
	private static final String CODICE_RISCATTO = "R123456";
	private static final String DATA_RISCATTO = "2025-01-28";

	private Ricompensa ricompensa;
	private Riscatto riscatto;

	@BeforeEach
	public void setUp() {
		ricompensa = new Ricompensa(NOME_RICOMPENSA, VALORE_RICOMPENSA, DESCRIZIONE_RICOMPENSA,
				DATA_SCADENZA_RICOMPENSA);
		riscatto = new Riscatto(ricompensa, ID_UTENTE, PUNTI, CODICE_RISCATTO, DATA_RISCATTO);
	}

	@Test
	public void testRicompensaAssociata() {
		assertNotNull(riscatto.getNomeRicompensa());
		assertNotNull(riscatto.getDescrizioneRicompensa());
		assertTrue(riscatto.getValoreRicompensa() > 0);
		assertNotNull(riscatto.getDataScadenzaRicompensa());
	}

	@Test
	public void testCreazioneRiscatto() {
		assertNotNull(riscatto, "L'oggetto riscatto non deve essere null");

		assertEquals(ricompensa.getNome(), riscatto.getNomeRicompensa());
		assertEquals(ricompensa.getDescrizione(), riscatto.getDescrizioneRicompensa());
		assertEquals(ricompensa.getValore(), riscatto.getValoreRicompensa());
		assertEquals(ricompensa.getDataScadenza(), riscatto.getDataScadenzaRicompensa());
	}

}

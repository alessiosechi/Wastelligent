package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import logic.model.domain.*;
import org.junit.jupiter.api.BeforeEach;

public class TestViewIniziale {

	private UtenteBase utenteBase;
	private OperatoreEcologico operatoreEcologico;
	private EspertoEcologico espertoEcologico;

	private static final String VIEW_UTENTE_BASE_1 = "EffettuaSegnalazioneView.fxml";
	private static final String VIEW_UTENTE_BASE_2 = "/logic/boundary2/SegnalaRifiutiView.fxml";

	private static final String VIEW_OPERATORE_ECOLOGICO_1 = "SegnalazioniAssegnateView.fxml";
	private static final String VIEW_OPERATORE_ECOLOGICO_2 = "";

	private static final String VIEW_ESPERO_ECOLOGICO_1 = "GestisciSegnalazioniView.fxml";
	private static final String VIEW_ESPERO_ECOLOGICO_2 = "GestisciSegnalazioniView2.fxml";

	@BeforeEach
	public void setUp() {
		utenteBase = new UtenteBase(1, "utenteBase");
		operatoreEcologico = new OperatoreEcologico(2, "operatoreEcologico");
		espertoEcologico = new EspertoEcologico(3, "espertoEcologico");
	}

	@Test
	public void testViewInizialeUtenteBase() {
		assertEquals(VIEW_UTENTE_BASE_1, utenteBase.getViewIniziale(1));
		assertEquals(VIEW_UTENTE_BASE_2, utenteBase.getViewIniziale(2));
	}

	@Test
	public void testViewInizialeOperatoreEcologico() {
		assertEquals(VIEW_OPERATORE_ECOLOGICO_1, operatoreEcologico.getViewIniziale(1));
		assertEquals(VIEW_OPERATORE_ECOLOGICO_2, operatoreEcologico.getViewIniziale(2));
	}

	@Test
	public void testViewInizialeEspertoEcologico() {
		assertEquals(VIEW_ESPERO_ECOLOGICO_1, espertoEcologico.getViewIniziale(1));
		assertEquals(VIEW_ESPERO_ECOLOGICO_2, espertoEcologico.getViewIniziale(2));
	}
}

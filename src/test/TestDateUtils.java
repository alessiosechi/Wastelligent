package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import logic.utils.DateUtils;

class TestDateUtils {

	@Test
	void testIsOggi() {
		String oggi = java.time.LocalDate.now().toString();
		assertTrue(DateUtils.isOggi(oggi));
	}

	@Test
	void testIsNotOggi() {
		String ieri = java.time.LocalDate.now().minusDays(1).toString();
		assertFalse(DateUtils.isOggi(ieri));
	}

}

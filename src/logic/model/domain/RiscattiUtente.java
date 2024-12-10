package logic.model.domain;

import java.util.ArrayList;
import java.util.List;

import logic.observer.Subject;

public class RiscattiUtente extends Subject {

	private List<Riscatto> riscatti = new ArrayList<>();
	private static RiscattiUtente instance;

	public static RiscattiUtente getInstance() {
		if (instance == null) {
			instance = new RiscattiUtente();
		}
		return instance;
	}

	public void aggiungiRiscatto(Riscatto riscatto) {
		riscatti.add(riscatto);
		notificaOsservatori();
	}

	public void setRiscatti(List<Riscatto> riscatti) {
		this.riscatti = riscatti;
	}

	public List<Riscatto> getRiscatti() {
		return riscatti;
	}
}

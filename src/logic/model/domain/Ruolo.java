package logic.model.domain;

public enum Ruolo {
	UTENTE_BASE(1), 
	ESPERTO_ECOLOGICO(2), 
	OPERATORE_ECOLOGICO(3);

	private final int id;

	private Ruolo(int id) {
		this.id = id;
	}

	public static Ruolo fromInt(int id) {
		for (Ruolo type : values()) {
			if (type.getId() == id) {
				return type;
			}
		}
		return null;
	}

	public int getId() {
		return id;
	}
}

package logic.decorator;

// Component

public interface ValidaInput {
	boolean valida(String input);

	String getMessaggioErrore();
}

package exceptions;

public class ConnessioneAPIException extends Exception{

    private static final long serialVersionUID = 1L;
	

	public ConnessioneAPIException(String message, Throwable cause) {
		super(message, cause);
	}
}

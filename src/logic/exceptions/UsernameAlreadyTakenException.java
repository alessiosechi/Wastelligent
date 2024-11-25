package logic.exceptions;

public class UsernameAlreadyTakenException extends Exception{

	
	private static final long serialVersionUID = 123456789L;
	

	public UsernameAlreadyTakenException(String message) {
		super(message);
	}
}

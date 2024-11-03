package exceptions;

public class UsernameAlreadyTakenException extends Exception{

	
	private static final long serialVersionUID = 123456789L; // Replace with a unique identifier
	
	
	public UsernameAlreadyTakenException() {

	}

	public UsernameAlreadyTakenException(String message) {
		super(message);
	}
}

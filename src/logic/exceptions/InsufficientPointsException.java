package logic.exceptions;

public class InsufficientPointsException extends Exception{
	
	
	

    
    /**
	 * 
	 */
	private static final long serialVersionUID = 6468651462029109638L;


	public InsufficientPointsException(String message) {
        super(message); // chiamo il costrutore della classe Exception e gli passo message
    }
}

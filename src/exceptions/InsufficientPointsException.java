package exceptions;

public class InsufficientPointsException extends Exception{
    private static final long serialVersionUID = 1L;
    public InsufficientPointsException(String message) {
        super(message);
    }
}

package exceptions;

public class DailyRedemptionLimitException extends Exception {
    private static final long serialVersionUID = 1L;
    public DailyRedemptionLimitException(String message) {
        super(message);
    }
}

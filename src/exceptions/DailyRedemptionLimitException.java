package exceptions;

public class DailyRedemptionLimitException extends Exception {
    public DailyRedemptionLimitException(String message) {
        super(message);
    }
}

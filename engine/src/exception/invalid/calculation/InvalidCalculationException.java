package exception.invalid.calculation;

public class InvalidCalculationException extends RuntimeException {
    public InvalidCalculationException(String message) {
        super(message);
    }
}

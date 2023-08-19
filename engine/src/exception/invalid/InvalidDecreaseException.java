package exception.invalid;

public class InvalidDecreaseException extends RuntimeException {
    public InvalidDecreaseException(String message) {
        super(message);
    }
}
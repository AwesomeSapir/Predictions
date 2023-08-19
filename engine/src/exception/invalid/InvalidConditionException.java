package exception.invalid;

public class InvalidConditionException extends RuntimeException {
    public InvalidConditionException(String message) {
        super(message);
    }
}

package exception.duplicate;

public class DuplicatePropertyException extends RuntimeException {
    public DuplicatePropertyException(String message) {
        super(message);
    }
}
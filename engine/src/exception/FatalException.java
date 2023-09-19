package exception;

public class FatalException extends EngineException {
    public FatalException(String message) {
        super(message, "Fatal");
    }
}

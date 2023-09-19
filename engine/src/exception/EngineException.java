package exception;

public class EngineException extends Exception{

    private final String type;

    public EngineException(String message, String type) {
        super(message);
        this.type = type;
    }

    public String getType() {
        return type;
    }
}

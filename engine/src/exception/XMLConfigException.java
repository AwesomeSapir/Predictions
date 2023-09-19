package exception;

public class XMLConfigException extends EngineException {

    public XMLConfigException(String message) {
        super(message, "Invalid XML Configuration");
    }
}

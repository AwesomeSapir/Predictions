package exception.invalid.calculation.divide;
import engine.world.validation.exception.invalid.calculation.InvalidCalculationException;
public class InvalidDivideException extends InvalidCalculationException{
    public InvalidDivideException(String message) {
        super(message);
    }
}

package engine.simulation.world.expression;

import java.util.ArrayList;
import java.util.List;

public class RawExpression {
    private final String value;
    private final List<String> subexpressions;

    public RawExpression(String value) {
        this.value = value;
        subexpressions = new ArrayList<>();
    }

    public void addExpression(String expression){
        subexpressions.add(expression);
    }

    public String getValue() {
        return value;
    }

    public List<String> getSubexpressions() {
        return subexpressions;
    }
}

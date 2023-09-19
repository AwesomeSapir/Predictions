package engine.simulation.world.rule.action.type.condition;

import exception.XMLConfigException;

public enum Operator {
    neq("!="), eq("="), bt("bt"), lt("lt");

    private final String operator;
    Operator(String s) {
        operator = s;
    }
    public String getOperator() {
        return operator;
    }

    public static Operator fromDRP(String value) throws XMLConfigException {
        for (Operator operator : Operator.values()) {
            if (operator.getOperator().equals(value)) {
                return operator;
            }
        }
        throw new XMLConfigException("Unknown XML operator value: " + value);
    }
}

package world.expression;

import world.Context;
import world.definition.property.PropertyDefinition;
import world.definition.property.PropertyType;

import java.util.function.Predicate;

public class ExpressionDecoder {

    public static Expression decodeExpression(String expressionString, String propertyName, Context context) {
        String[] words = expressionString.split("\\(");
        String firstWord = words[0];
        String secondWord = words[1].substring(0, words[1].length() - 1);

        try {
            FunctionType type = FunctionType.valueOf(firstWord.toUpperCase());
            // Check and handle auxiliary function expression
            switch (type) {
                case ENVIRONMENT:
                    return new EnvironmentExpression(context, secondWord);
                case RANDOM:
                    int range = Integer.parseInt(secondWord);
                    return new RandomExpression(range);
                default:
                    throw new IllegalArgumentException("Function type not supported");
            }
        } catch (IllegalArgumentException e) {
            if (context.getPrimaryEntityDefinition().getProperties().values().stream().anyMatch(new Predicate<PropertyDefinition>() {
                @Override
                public boolean test(PropertyDefinition propertyDefinition) {
                    return propertyDefinition.getName().equals(expressionString);
                }
            })) {
                return new EntityPropertyExpression(expressionString);
            } else {
                PropertyType type = context.getPrimaryEntityDefinition().getProperties().get(propertyName).getType();
                return new FreeValueExpression(expressionString, type);
            }
        }
    }
}

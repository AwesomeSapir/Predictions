package world.expression;

import world.Context;
import world.instance.property.PropertyInstance;
import world.rule.action.Action;

import java.util.Set;

public class ExpressionDecoder {

    public Expression decodeExpression(String expressionString, Context context, Action action) {
        String[] words = expressionString.split("\\(");
        String firstWord = words[0];
        String secondWord = words[1].substring(0, words[1].length() - 1);

        try {
            FunctionType type = FunctionType.valueOf(firstWord.toUpperCase());
            // Check and handle auxiliary function expression
            switch (type) {
                case ENVIRONMENT:
                    return new EnvironmentExpression(context, secondWord);
                break;
                case RANDOM:
                    int range = Integer.parseInt(secondWord);
                    return new RandomExpression(range);
                break;
            }
        }
       catch(IllegalArgumentException e){
        }
        try{
            PropertyInstance propertyInstance = context.getPrimaryEntityInstances().;
        }
    }

    // Other methods for initializing sets, adding names, etc.
}

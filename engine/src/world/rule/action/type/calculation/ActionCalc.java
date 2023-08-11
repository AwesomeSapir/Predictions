package world.rule.action.type.calculation;

import engine.prd.PRDAction;
import validator.Validator;
import world.Context;
import world.expression.EntityPropertyExpression;
import world.expression.Expression;
import world.expression.ExpressionDecoder;
import world.rule.action.Action;

public abstract class ActionCalc extends Action {

    protected String resultPropertyName;
    protected Expression arg1;
    protected Expression arg2;

    public ActionCalc(PRDAction prdObject, Context context) throws Exception{
        super(prdObject);
        if(!context.getPrimaryEntityDefinition().getName().equals(entityName))
            throw new IllegalArgumentException("Entity '" + entityName + "' referenced in Calculation does not exist.");

        resultPropertyName = prdObject.getResultProp();

        if(context.getPrimaryEntityDefinition().getProperties().get(resultPropertyName) == null)
            throw new IllegalArgumentException("Property '" + resultPropertyName + "' referenced result property in Calculation does not exist.");
        if(!context.getPrimaryEntityDefinition().getProperties().get(resultPropertyName).isNumeric())
            throw new IllegalArgumentException("Property '" + resultPropertyName + "' referenced to result property in Calculation is not of a numeric type.");

        arg1 = ExpressionDecoder.decodeExpression(prdObject.getPRDMultiply().getArg1(), prdObject.getResultProp(), context);

        if(arg1 instanceof EntityPropertyExpression){
            if(!context.getPrimaryEntityDefinition().getProperties().get(arg1.toString()).isNumeric())
                throw new IllegalArgumentException("Property '" + arg1 + "' referenced to arg1 in Calculation is not of a numeric type.");
        }

        arg2 = ExpressionDecoder.decodeExpression(prdObject.getPRDMultiply().getArg2(), prdObject.getResultProp(), context);

        if(arg2 instanceof EntityPropertyExpression){
            if(!context.getPrimaryEntityDefinition().getProperties().get(arg2.toString()).isNumeric())
                throw new IllegalArgumentException("Property '" + arg2 + "' referenced to arg2 in Calculation is not of a numeric type.");
        }
    }
}

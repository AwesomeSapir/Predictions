package world.rule.action.type.value;

import engine.prd.PRDAction;
import validator.Validator;
import world.Context;
import world.expression.Expression;
import world.expression.ExpressionDecoder;
import world.rule.action.Action;

public abstract class ActionValue extends Action {

    protected String propertyName;
    protected Expression value;

    public ActionValue(PRDAction prdObject, Context context) throws Exception{
        super(prdObject);
        propertyName = prdObject.getProperty();

        if(!context.getPrimaryEntityDefinition().getName().equals(entityName))
            throw new IllegalArgumentException("Entity '" + entityName + "' referenced in '" + type + "' does not exist.");

        if(!context.getPrimaryEntityDefinition().getName().equals(propertyName))
            throw new IllegalArgumentException("Property '" + propertyName + "' referenced in '" + type + "' does not exist.");

        //TODO when removing prd, send getBy or getValue depending on if set or increase
        //Check if it is upperCase or not
        if(type.toString().equalsIgnoreCase("SET"))
        value = ExpressionDecoder.decodeExpression(prdObject.getValue(), propertyName, context);
        else
            value = ExpressionDecoder.decodeExpression(prdObject.getBy(), propertyName, context);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public Expression getValue() {
        return value;
    }
}

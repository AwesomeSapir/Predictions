package world.rule.action.type;

import engine.prd.PRDAction;
import validator.Validator;
import world.Context;
import world.instance.entity.EntityInstance;
import world.rule.action.Action;

public class ActionKill extends Action {

    public ActionKill(PRDAction prdObject, Context context) {
        super(prdObject);
        if(!context.getPrimaryEntityDefinition().getName().equals(entityName))
            throw new IllegalArgumentException("Entity '" + entityName + "' referenced in '" + type + "' does not exist.");
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) throws Exception{
        context.removeEntity(entityInstance);
    }
}

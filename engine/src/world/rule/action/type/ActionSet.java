package world.rule.action.type;

import engine.prd.PRDAction;
import world.Context;
import world.Entity;
import world.instance.entity.EntityInstance;
import world.instance.property.PropertyInstance;
import world.property.Property;
import world.rule.action.Action;

public class ActionSet extends Action {

    protected String propertyName;
    protected String value;

    public ActionSet(PRDAction prdObject) {
        super(prdObject);
    }

    @Override
    public void execute(Context context) {

    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

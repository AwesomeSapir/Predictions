package world.rule.action.type;

import engine.prd.PRDAction;
import world.instance.entity.EntityInstance;
import world.instance.property.PropertyInstance;
import world.rule.action.Action;

public class ActionIncrease extends Action {

    protected String propertyName;
    protected String by;

    public ActionIncrease(PRDAction prdObject) {
        super(prdObject);
        propertyName = prdObject.getProperty();
        by = prdObject.getBy();
    }

    @Override
    public void execute(EntityInstance entity) {
        PropertyInstance property = entity.getPropertyByName(propertyName);
        double increaseValue = Double.parseDouble(by);
        double newValue = (double) property.getValue() + increaseValue;
        property.setValue(newValue);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getBy() {
        return by;
    }

    public void setBy(String by) {
        this.by = by;
    }
}

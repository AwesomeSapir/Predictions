package dto.detail.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class DTOActionCondition extends DTOAction{

    protected final String conditions;
    protected final List<DTOAction> actionsThen = new ArrayList<>();
    protected final List<DTOAction> actionsElse = new ArrayList<>();

    public DTOActionCondition(String type, String entityName, String conditions, Collection<DTOAction> actionsThen, Collection<DTOAction> actionsElse) {
        super(type, entityName);
        this.conditions = conditions;
        this.actionsThen.addAll(actionsThen);
        this.actionsElse.addAll(actionsElse);
    }

    public String getConditions() {
        return conditions;
    }

    public List<DTOAction> getActionsThen() {
        return actionsThen;
    }

    public List<DTOAction> getActionsElse() {
        return actionsElse;
    }

    @Override
    public Map<String, String> getFieldValueMap() {
        Map<String, String> fieldValues = super.getFieldValueMap();
        fieldValues.put("Conditions", conditions);
        fieldValues.put("Then", actionsThen.toString());
        fieldValues.put("Else", actionsElse.toString());
        return fieldValues;
    }
}

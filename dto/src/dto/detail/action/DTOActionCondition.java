package dto.detail.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DTOActionCondition extends DTOAction{

    protected final String conditions;
    protected final List<DTOAction> actionsThen = new ArrayList<>();
    protected final List<DTOAction> actionsElse = new ArrayList<>();

    public DTOActionCondition(String type, String entityName, DTOSecondaryEntity secondaryEntity, String conditions, Collection<DTOAction> actionsThen, Collection<DTOAction> actionsElse) {
        super(type, entityName, secondaryEntity);
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
}

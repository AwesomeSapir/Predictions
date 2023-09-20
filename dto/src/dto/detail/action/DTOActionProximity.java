package dto.detail.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DTOActionProximity extends DTOAction{

    private final String target;
    private final String depth;
    protected final List<DTOAction> actions = new ArrayList<>();

    public DTOActionProximity(String type, String entityName, DTOSecondaryEntity secondaryEntity, String target, String depth, Collection<DTOAction> actions) {
        super(type, entityName, secondaryEntity);
        this.target = target;
        this.depth = depth;
        this.actions.addAll(actions);
    }

    public String getTarget() {
        return target;
    }

    public String getDepth() {
        return depth;
    }

    public List<DTOAction> getActions() {
        return actions;
    }
}

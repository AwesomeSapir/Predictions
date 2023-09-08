package engine.world.rule.action;

import engine.world.Context;
import engine.world.definition.entity.EntityDefinition;
import engine.world.instance.entity.EntityInstance;

import java.io.Serializable;

public abstract class Action implements Serializable {

    protected final ActionType type;
    protected final EntityDefinition primaryEntity;
    protected final EntityDefinition secondaryEntity;

    protected final int selectionCount;

    public Action(ActionType type, EntityDefinition primaryEntity) {
        this.type = type;
        this.primaryEntity = primaryEntity;
        this.secondaryEntity = null; //TODO
        this.selectionCount = 0; //TODO
    }

    public abstract void execute(EntityInstance entityInstance, Context context);

    public void execute(EntityInstance primaryEntity, EntityInstance secondaryEntity, Context context){} //TODO

    public ActionType getType() {
        return type;
    }

    public EntityDefinition getPrimaryEntity() {
        return primaryEntity;
    }

    public EntityDefinition getSecondaryEntity() {
        return secondaryEntity;
    }

    public int getSelectionCount() {
        return selectionCount;
    }
}

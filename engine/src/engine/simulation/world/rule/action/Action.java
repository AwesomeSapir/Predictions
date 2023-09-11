package engine.simulation.world.rule.action;

import engine.simulation.world.Context;
import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.instance.entity.EntityInstance;

import java.io.Serializable;

public abstract class Action implements Serializable {

    protected final ActionType type;
    protected final EntityDefinition primaryEntity;
    protected final EntityDefinition secondaryEntity;

    protected final int selectionCount;

    public Action(ActionType type, EntityDefinition primaryEntity, EntityDefinition secondaryEntity) {
        this.type = type;
        this.primaryEntity = primaryEntity;
        this.secondaryEntity = secondaryEntity;
        this.selectionCount = 0; //TODO
    }

    public abstract void execute(EntityInstance entityInstance, Context context);

    public void execute(EntityInstance primaryEntity, EntityInstance secondaryEntity, Context context){} //TODO make abstact

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

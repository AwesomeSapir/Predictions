package engine.simulation.world.rule.action;

import engine.simulation.world.Context;
import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.instance.entity.EntityInstance;

import java.io.Serializable;

public abstract class Action implements Serializable {

    protected final ActionType type;
    protected final EntityDefinition primaryEntity;
    protected final SecondaryEntity secondaryEntity;

    public Action(ActionType type, EntityDefinition primaryEntity, SecondaryEntity secondaryEntity) {
        this.type = type;
        this.primaryEntity = primaryEntity;
        this.secondaryEntity = secondaryEntity;
    }

    public abstract void execute(EntityInstance entityInstance, Context context);

    public abstract void execute(EntityInstance primaryEntity, EntityInstance secondaryEntity, Context context);

    public ActionType getType() {
        return type;
    }

    public EntityDefinition getPrimaryEntity() {
        return primaryEntity;
    }

    public SecondaryEntity getSecondaryEntity() {
        return secondaryEntity;
    }
}

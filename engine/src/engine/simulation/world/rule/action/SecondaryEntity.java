package engine.simulation.world.rule.action;

import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.rule.action.type.condition.MultiCondition;

public class SecondaryEntity {

    private final EntityDefinition entityDefinition;
    private final int selectionCount;
    private final boolean all;
    private final MultiCondition condition;

    public SecondaryEntity(EntityDefinition entityDefinition, int selectionCount, MultiCondition condition) {
        this.entityDefinition = entityDefinition;
        this.selectionCount = selectionCount;
        this.all = false;
        this.condition = condition;
    }

    public SecondaryEntity(EntityDefinition entityDefinition) {
        this.entityDefinition = entityDefinition;
        this.selectionCount = -1;
        this.all = true;
        this.condition = null;
    }

    public EntityDefinition getEntityDefinition() {
        return entityDefinition;
    }

    public int getSelectionCount() {
        return selectionCount;
    }

    public boolean isAll() {
        return all;
    }

    public MultiCondition getCondition() {
        return condition;
    }
}

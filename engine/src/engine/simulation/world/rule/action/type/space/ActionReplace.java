package engine.simulation.world.rule.action.type.space;

import engine.simulation.world.Context;
import engine.simulation.world.definition.entity.EntityDefinition;
import engine.simulation.world.definition.property.PropertyDefinition;
import engine.simulation.world.instance.entity.EntityInstance;
import engine.simulation.world.instance.property.PropertyInstance;
import engine.simulation.world.rule.action.Action;
import engine.simulation.world.rule.action.ActionType;

public class ActionReplace extends Action {

    protected final ReplaceMode mode;

    /*
    * primary = kill
    * secondary = create
    */
    public ActionReplace(EntityDefinition primaryEntity, EntityDefinition secondaryEntity, ReplaceMode mode) {
        super(ActionType.replace, primaryEntity, secondaryEntity);
        this.mode = mode;
    }

    private void derived(EntityInstance oldEntity, EntityInstance newEntity){
        for (PropertyDefinition propertyDefinition : primaryEntity.getProperties().values()){
            PropertyInstance createdProperty = newEntity.getPropertyIfExists(propertyDefinition.getName());
            if (createdProperty != null){
                if(createdProperty.getPropertyDefinition().getType().equals(propertyDefinition.getType())){
                    createdProperty.setValue(oldEntity.getPropertyIfExists(propertyDefinition.getName()).getValue());
                }
            }
        }
    }

    @Override
    public void execute(EntityInstance entityInstance, Context context) {
        if(entityInstance.getEntityDefinition().equals(primaryEntity)){
            EntityInstance created = new EntityInstance(secondaryEntity);
            created.initProperties();
            if(mode == ReplaceMode.DERIVED){
                derived(entityInstance, created);
            }
            context.getEntityManager().replaceEntity(entityInstance, created);
            context.getSpaceManager().replaceEntity(entityInstance, created);
        }
    }
}

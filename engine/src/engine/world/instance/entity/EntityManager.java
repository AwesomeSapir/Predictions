package engine.world.instance.entity;

import engine.world.definition.entity.EntityDefinition;

import java.util.*;

public class EntityManager {

    protected final Map<String, EntityDefinition> entityDefinitions = new HashMap<>();
    protected final Map<EntityDefinition, List<EntityInstance>> entityInstances = new HashMap<>();

    public EntityManager(Collection<EntityDefinition> entityDefinitions) {
        for (EntityDefinition entityDefinition : entityDefinitions){
            this.entityDefinitions.put(entityDefinition.getName(), entityDefinition);
            this.entityInstances.put(entityDefinition, new ArrayList<>());
        }
    }

    public void setPopulation(EntityDefinition entityDefinition, int population){
        for (int i = 0; i < population; i++) {
            EntityInstance entityInstance = new EntityInstance(entityDefinition);
            entityInstance.initProperties();
            entityInstances.get(entityDefinition).add(entityInstance);
        }
    }

    public void removeEntity(EntityInstance entityInstance){
        entityInstances.get(entityInstance.getEntityDefinition()).remove(entityInstance);
    }

    public EntityDefinition getEntityDefinition(String name) {
        return entityDefinitions.get(name);
    }

    public Collection<EntityInstance> getEntityInstances(EntityDefinition entityDefinition) {
        return entityInstances.get(entityDefinition);
    }

    public Collection<EntityDefinition> getEntityDefinitions(){
        return entityDefinitions.values();
    }
}

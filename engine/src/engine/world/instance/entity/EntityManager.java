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

    public EntityDefinition getEntityDefinition(String name) throws RuntimeException{
        if(!containsEntityDefinition(name)){
            throw new RuntimeException("Entity '" + name + "' does not exist.");
        }
        return entityDefinitions.get(name);
    }

    public Collection<EntityInstance> getEntityInstances(EntityDefinition entityDefinition) {
        return entityInstances.get(entityDefinition);
    }

    public Collection<EntityInstance> getEntityInstances(EntityDefinition entityDefinition, int count) {
        List<EntityInstance> entityInstances = new ArrayList<>(getEntityInstances(entityDefinition));
        List<EntityInstance> result = new ArrayList<>();
        count = Math.min(count, entityInstances.size());
        for (int i=0; i<count; i++){
            result.add(entityInstances.get(i));
        }
        return result;
    }

    public Collection<EntityDefinition> getAllEntityDefinitions(){
        return entityDefinitions.values();
    }

    public Collection<EntityInstance> getAllEntityInstances(){
        List<EntityInstance> result = new ArrayList<>();
        for (List<EntityInstance> entityInstances : entityInstances.values()){
            result.addAll(entityInstances);
        }
        return result;
    }

    public boolean containsEntityDefinition(String name){
        return entityDefinitions.containsKey(name);
    }
}

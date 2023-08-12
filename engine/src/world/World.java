package world;

import world.definition.entity.EntityDefinition;
import world.instance.entity.EntityInstance;
import world.instance.environment.ActiveEnvironment;
import world.instance.environment.EnvironmentManager;
import world.instance.property.PropertyInstance;
import world.rule.Rule;
import world.termination.Termination;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class World implements Context {

    protected EnvironmentManager environmentManager;
    protected ActiveEnvironment activeEnvironment;
    protected EntityDefinition primaryEntityDefinition;
    protected List<EntityInstance> entityInstances = new ArrayList<>();
    protected Map<String, Rule> rules;
    protected Termination termination;

    public World(EnvironmentManager environmentManager, ActiveEnvironment activeEnvironment, EntityDefinition primaryEntityDefinition, Collection<EntityInstance> entityInstances, Map<String, Rule> rules, Termination termination) {
        this.environmentManager = environmentManager;
        this.activeEnvironment = activeEnvironment;
        this.primaryEntityDefinition = primaryEntityDefinition;
        this.entityInstances.addAll(entityInstances);
        this.rules = rules;
        this.termination = termination;
    }

    public Map<String, Rule> getRules() {
        return rules;
    }

    public Termination getTermination() {
        return termination;
    }

    public void setTermination(Termination termination) {
        this.termination = termination;
    }

    @Override
    public PropertyInstance getEnvironmentPropertyInstance(String name) {
        return activeEnvironment.getProperty(name);
    }

    @Override
    public void removeEntity(EntityInstance entityInstance) {
        entityInstances.remove(entityInstance);
    }

    @Override
    public EntityDefinition getPrimaryEntityDefinition() {
        return primaryEntityDefinition;
    }

    @Override
    public Collection<EntityInstance> getPrimaryEntityInstances() {
        return entityInstances;
    }
}

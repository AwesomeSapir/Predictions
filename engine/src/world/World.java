package world;

import engine.prd.PRDEntity;
import engine.prd.PRDRule;
import engine.prd.PRDWorld;
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
    protected List<EntityInstance> entityInstances;
    protected Map<String, Rule> rules;
    protected Termination termination;

    public World(PRDWorld prdObject) {
        // Environment initialization
        environmentManager = new EnvironmentManager(prdObject.getPRDEvironment());
        activeEnvironment = environmentManager.createActiveEnvironment();
        activeEnvironment.initProperties(environmentManager.getVariables());

        primaryEntityDefinition = new EntityDefinition(prdObject.getPRDEntities().getPRDEntity().get(0));
        entityInstances = new ArrayList<>();

        // Entity instances initialization
        for (int i = 0; i < primaryEntityDefinition.getPopulation(); i++) {
            EntityInstance entityInstance = new EntityInstance(primaryEntityDefinition, i + 1);
            entityInstance.initProperties();
            entityInstances.add(entityInstance);
        }

        // Rule initialization
        for (PRDRule rule : prdObject.getPRDRules().getPRDRule()) {
            rules.put(rule.getName(), new Rule(rule));
        }
        termination = new Termination(prdObject.getPRDTermination());
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
    public Collection<EntityInstance> getPrimaryEntityInstances() {
        return entityInstances;
    }
}

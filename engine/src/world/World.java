package world;

import engine.prd.PRDEntity;
import engine.prd.PRDRule;
import engine.prd.PRDWorld;
import world.definition.entity.EntityDefinition;
import world.instance.entity.EntityInstance;
import world.instance.environment.ActiveEnvironment;
import world.instance.environment.EnvironmentManager;
import world.rule.Rule;
import world.termination.Termination;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class World {

    protected EnvironmentManager environmentManager;
    protected ActiveEnvironment activeEnvironment;
    protected Map<String, EntityDefinition> entityDefinitions;
    protected Map<String, List<EntityInstance>> entityInstances;
    protected Map<String, Rule> rules;
    protected Termination termination;

    public World(PRDWorld prdObject) {
        // Environment initialization
        environmentManager = new EnvironmentManager(prdObject.getPRDEvironment());
        activeEnvironment = environmentManager.createActiveEnvironment();
        activeEnvironment.initProperties(environmentManager.getVariables());

        // Entity definition initialization
        for (PRDEntity prdEntity : prdObject.getPRDEntities().getPRDEntity()){
            entityDefinitions.put(prdEntity.getName(), new EntityDefinition(prdEntity));
            entityInstances.put(prdEntity.getName(), new ArrayList<>());
        }

        // Entity instances initialization
        for (EntityDefinition entityDefinition : entityDefinitions.values()) {
            for (int i=0; i<entityDefinition.getPopulation(); i++){
                EntityInstance entityInstance = new EntityInstance(entityDefinition, i+1);
                entityInstance.initProperties();
                entityInstances.get(entityDefinition.getName()).add(entityInstance);
            }
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
}

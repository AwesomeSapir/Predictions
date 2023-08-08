package world;

import engine.prd.PRDEntity;
import engine.prd.PRDEnvProperty;
import engine.prd.PRDRule;
import engine.prd.PRDWorld;
import world.property.EnvProperty;
import world.rule.Rule;
import world.termination.Termination;

import java.util.List;
import java.util.Map;

public class World {

    protected Map<String, EnvProperty> envProperties;
    protected List<Entity> entities; //TODO figure out collection
    protected Map<String, Rule> rules;
    protected Termination termination;

    public World(PRDWorld prdObject) {
        for (PRDEnvProperty envProperty : prdObject.getPRDEvironment().getPRDEnvProperty()) {
            envProperties.put(envProperty.getPRDName(), new EnvProperty(envProperty));
        }
        for (PRDEntity entity : prdObject.getPRDEntities().getPRDEntity()) {
            for (int i=0; i<entity.getPRDPopulation(); i++){
                entities.add(new Entity(entity));
            }
        }
        for (PRDRule rule : prdObject.getPRDRules().getPRDRule()) {
            rules.put(rule.getName(), new Rule(rule));
        }
        termination = new Termination(prdObject.getPRDTermination());
    }

    public Map<String, EnvProperty> getEnvProperties() {
        return envProperties;
    }

    public List<Entity> getEntities() {
        return entities;
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

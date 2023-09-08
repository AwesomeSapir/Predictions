package engine;

import dto.detail.*;
import dto.detail.action.DTOAction;
import dto.detail.action.DTOActionCalc;
import dto.detail.action.DTOActionCondition;
import dto.detail.action.DTOActionValue;
import dto.simulation.*;
import engine.simulation.Simulation;
import engine.simulation.SimulationInterface;
import engine.world.World;
import engine.world.definition.entity.EntityDefinition;
import engine.world.definition.property.PropertyDefinition;
import engine.world.instance.entity.EntityInstance;
import engine.world.rule.Rule;
import engine.world.rule.action.Action;
import engine.world.rule.action.type.calculation.ActionCalc;
import engine.world.rule.action.type.condition.ActionCondition;
import engine.world.rule.action.type.value.ActionValue;
import engine.world.termination.BySecond;
import engine.world.termination.ByTicks;
import engine.world.termination.Termination;
import javafx.util.Pair;
import translation.xml.Translator;
import translation.xml.XmlTranslator;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.*;

public class Engine implements EngineInterface, Serializable {

    private final Map<Integer, SimulationInterface> pastSimulations = new LinkedHashMap<>();
    private SimulationInterface simulation = null;
    private String filepath;
    private int idCounter = 1;


    @Override
    public void loadXml(String filepath) {
        try {
            simulation = new Simulation(getWorldFromFile(filepath));
            this.filepath = filepath;
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Could not find a suitable XML file at path '" + filepath + "'");
        } catch (JAXBException | InvalidClassException e) {
            throw new RuntimeException(e);
        }
    }

    public World getWorldFromFile(String filepath) throws FileNotFoundException, JAXBException, InvalidClassException {
        InputStream inputStream = new FileInputStream(filepath);
        Translator translator = new XmlTranslator(inputStream);
        return translator.getWorld();
    }

    @Override
    public Collection<DTOEntityPopulation> getDetailsByEntityCount(int id) {
        World world = pastSimulations.get(id).getWorld();
        List<DTOEntityPopulation> entityPopulations = new ArrayList<>();

        for (EntityDefinition entityDefinition : world.getEntityDefinitions()){
            entityPopulations.add(new DTOEntityPopulation(
                    entityDefinition.getPopulation(),
                    world.getEntityInstances(entityDefinition).size(),
                    getEntity(entityDefinition)));
        }

        return entityPopulations;
    }

    private DTOEntity getEntity(EntityDefinition entityDefinition) {
        return new DTOEntity(entityDefinition.getName(), entityDefinition.getPopulation(), getProperties(entityDefinition));
    }

    private Collection<DTOEntity> getEntities(SimulationInterface simulation) {
        List<DTOEntity> entities = new ArrayList<>();
        EntityDefinition entityDefinition = simulation.getPrimaryEntityDefinition();
        entities.add(getEntity(entityDefinition));
        return entities;
    }

    @Override
    public Collection<DTOEntity> getPastEntities(int id) {
        return getEntities(pastSimulations.get(id));
    }

    private Collection<DTOProperty> getProperties(EntityDefinition entityDefinition) {
        List<DTOProperty> properties = new ArrayList<>();
        for (PropertyDefinition propertyDefinition : entityDefinition.getProperties().values()) {
            DTORange range = null;
            if (propertyDefinition.getRange() != null) {
                range = new DTORange(propertyDefinition.getRange().from(), propertyDefinition.getRange().to());
            }
            properties.add(new DTOProperty(propertyDefinition.getName(), propertyDefinition.getType().toString(), range, propertyDefinition.isRandomInit()));
        }
        return properties;
    }

    @Override
    public Collection<DTOProperty> getPastEntityProperties(int id, String name) {
        return getProperties(pastSimulations.get(id).getEntityDefinition(name));
    }

    @Override
    public DTOSimulationHistogram getValuesForPropertyHistogram(int id, String propertyName, String entityName) {
        List<Object> values = new ArrayList<>();
        World world = pastSimulations.get(id).getWorld();
        for (EntityInstance entityInstance : world.getEntityInstances(world.getEntityDefinition(entityName))) {
            values.add(entityInstance.getPropertyByName(propertyName).getValue());
        }
        return new DTOSimulationHistogram(values, propertyName);
    }

    @Override
    public Collection<DTOEnvironmentVariable> getEnvironmentDefinitions() throws NullPointerException{
        isSimulationLoaded();
        List<DTOEnvironmentVariable> environmentVariables = new ArrayList<>();
        for (PropertyDefinition propertyDefinition : simulation.getWorld().getEnvironmentManager().getVariables()) {
            DTORange range = null;
            if (propertyDefinition.getRange() != null) {
                range = new DTORange(propertyDefinition.getRange().from(), propertyDefinition.getRange().to());
            }
            environmentVariables.add(new DTOEnvironmentVariable(propertyDefinition.getName(), propertyDefinition.getType().toString(), range));
        }
        return environmentVariables;
    }

    @Override
    public Collection<DTOEnvironmentVariable> getEnvironmentValues() throws NullPointerException {
        isSimulationLoaded();
        List<DTOEnvironmentVariable> environmentVariables = new ArrayList<>();
        for(PropertyDefinition propertyDefinition : simulation.getWorld().getEnvironmentManager().getVariables()){
            Object value = simulation.getEnvironmentValue(propertyDefinition.getName());
            environmentVariables.add(new DTOEnvironmentVariable(propertyDefinition.getName(), value));
        }
        return environmentVariables;
    }

    @Override
    public void setEnvironmentValues(Collection<Pair<String, Object>> envValues) throws NullPointerException{
        isSimulationLoaded();
        List<Pair<String, Object>> values = new ArrayList<>(envValues);
        for (Pair<String, Object> envVar : values) {
            simulation.setEnvironmentValue(envVar.getKey(), envVar.getValue());
        }
    }

    @Override
    public DTOSimulationResult runSimulation() throws NullPointerException {
        isSimulationLoaded();
        int id = idCounter;
        archiveSimulation();
        pastSimulations.get(id).run(id);

        Termination termination = pastSimulations.get(id).getTermination();
        return new DTOSimulationResult(termination.isMetBySeconds(), termination.isMetByTicks(), id);
    }

    @Override
    public Collection<DTOSimulation> getPastSimulations() {
        List<DTOSimulation> simulations = new ArrayList<>();
        for (SimulationInterface simulation : pastSimulations.values()) {
            simulations.add(new DTOSimulation(simulation.getDate(), simulation.getId()));
        }
        return simulations;
    }

    @Override
    public DTOSimulationDetails getSimulationDetails() throws NullPointerException {
        isSimulationLoaded();

        List<DTORule> rules = new ArrayList<>();
        for (Rule rule : simulation.getWorld().getRules().values()) {
            rules.add(new DTORule(rule.getName(), rule.getActivation().getTicks(), rule.getActivation().getProbability(), getActions(rule.getActions())));
        }

        DTOTermination termination = new DTOTermination(
                Optional.ofNullable(simulation.getTermination().getBySecond()).map(BySecond::getCount).orElse(null),
                Optional.ofNullable(simulation.getTermination().getByTicks()).map(ByTicks::getCount).orElse(null));
        return new DTOSimulationDetails(getEntities(simulation), rules, termination);
    }

    private Collection<DTOAction> getActions(Collection<Action> actions){
        List<DTOAction> result = new ArrayList<>();
        for (Action action : actions) {
            DTOAction dtoAction;
            if(action instanceof ActionValue){
                ActionValue actionValue = (ActionValue) action;
                dtoAction = new DTOActionValue(actionValue.getType().toString(), actionValue.getPrimaryEntity().getName(), actionValue.getPropertyName(), actionValue.getValue().toString());
            } else if(action instanceof ActionCalc){
                ActionCalc actionCalc = (ActionCalc) action;
                dtoAction = new DTOActionCalc(actionCalc.getType().toString(), actionCalc.getPrimaryEntity().getName(), actionCalc.getResultPropertyName(), actionCalc.getArg1().toString(), actionCalc.getArg2().toString());
            } else if(action instanceof ActionCondition){
                ActionCondition actionCondition = (ActionCondition) action;
                dtoAction = new DTOActionCondition(actionCondition.getType().toString(), actionCondition.getPrimaryEntity().getName(), actionCondition.getConditions().toString(), getActions(actionCondition.getActionsThen()), getActions(actionCondition.getActionsElse()));
            } else {
                dtoAction = new DTOAction(action.getType().toString(), action.getPrimaryEntity().getName());
            }
            result.add(dtoAction);
        }
        return result;
    }

    private void archiveSimulation() {
        pastSimulations.put(idCounter++, simulation);
        try {
            simulation = new Simulation(getWorldFromFile(filepath));
        } catch (FileNotFoundException | JAXBException | InvalidClassException e) {
            throw new RuntimeException(e);
        }
    }

    private void isSimulationLoaded() throws NullPointerException {
        if (simulation == null) {
            throw new NullPointerException("No simulation is loaded.");
        }
    }

    @Override
    public void saveToFile(String filepath) {
        try (ObjectOutputStream encoder = new ObjectOutputStream(new FileOutputStream(filepath.concat(".file")))){
            encoder.writeObject(this);
            encoder.flush();
        } catch (Exception e) {
            throw new RuntimeException("Error saving to file: " + e.getMessage());
        }
    }

    @Override
    public void loadFromFile(String filepath) {
        try (ObjectInputStream decoder = new ObjectInputStream(new FileInputStream(filepath.concat(".file")))){
            Engine engine = (Engine) decoder.readObject();
            this.pastSimulations.putAll(engine.pastSimulations);
            this.simulation = engine.simulation;
            this.idCounter = engine.idCounter;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error loading from file: " + e.getMessage());
        }
    }

    @Override
    public int getNextId() {
        return idCounter;
    }

    @Override
    public DTOStatus getSimulationStatus(int id){
        System.out.println("duration: " + pastSimulations.get(id).getDuration());
        return new DTOStatus(pastSimulations.get(id).getTick(), pastSimulations.get(id).getDuration());
    }

    @Override
    public DTOTermination getSimulationTermination() {
        return new DTOTermination(
                Optional.ofNullable(simulation.getTermination().getBySecond()).map(BySecond::getCount).orElse(null),
                Optional.ofNullable(simulation.getTermination().getByTicks()).map(ByTicks::getCount).orElse(null));
    }

    @Override
    public void pauseSimulation(int id) {
        pastSimulations.get(id).pause();
    }

    @Override
    public DTOSimulationResult resumeSimulation(int id) {
        pastSimulations.get(id).resume();
        Termination termination = pastSimulations.get(id).getTermination();
        return new DTOSimulationResult(termination.isMetBySeconds(), termination.isMetByTicks(), id);
    }
}

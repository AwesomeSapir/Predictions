import dto.detail.*;
import dto.simulation.*;
import javafx.util.Pair;
import translation.xml.Translator;
import translation.xml.XmlTranslator;
import world.World;
import world.definition.entity.EntityDefinition;
import world.definition.property.PropertyDefinition;
import world.instance.entity.EntityInstance;
import world.rule.Rule;
import world.rule.action.Action;
import world.termination.Termination;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.util.*;

public class Engine implements EngineInterface, Serializable {

    private final Map<Integer, Simulation> pastSimulations = new LinkedHashMap<>();
    private Simulation simulation = null;
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

        entityPopulations.add(new DTOEntityPopulation(world.getPrimaryEntityDefinition().getPopulation(), world.getPrimaryEntityInstances().size(), getEntity(world.getPrimaryEntityDefinition())));
        return entityPopulations;
    }

    private DTOEntity getEntity(EntityDefinition entityDefinition) {
        return new DTOEntity(entityDefinition.getName(), entityDefinition.getPopulation(), getProperties(entityDefinition));
    }

    private Collection<DTOEntity> getEntities(Simulation simulation) {
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
        return getProperties(pastSimulations.get(id).getPrimaryEntityDefinition());
    }

    @Override
    public DTOSimulationHistogram getValuesForPropertyHistogram(int id, String name) {
        List<Object> values = new ArrayList<>();
        for (EntityInstance entityInstance : pastSimulations.get(id).getWorld().getPrimaryEntityInstances()) {
            values.add(entityInstance.getPropertyByName(name).getValue());
        }
        return new DTOSimulationHistogram(values, name);
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
        simulation.run(idCounter);
        int id = idCounter;
        archiveSimulation();

        Termination termination = simulation.getTermination();
        return new DTOSimulationResult(termination.isMetBySeconds(), termination.isMetByTicks(), id);
    }

    @Override
    public Collection<DTOSimulation> getPastSimulations() {
        List<DTOSimulation> simulations = new ArrayList<>();
        for (Simulation simulation : pastSimulations.values()) {
            simulations.add(new DTOSimulation(simulation.getDate(), simulation.getId()));
        }
        return simulations;
    }

    @Override
    public DTOSimulationDetails getSimulationDetails() throws NullPointerException {
        isSimulationLoaded();

        List<DTORule> rules = new ArrayList<>();
        for (Rule rule : simulation.getWorld().getRules().values()) {
            List<String> actionNames = new ArrayList<>();
            for (Action action : rule.getActions()) {
                actionNames.add(action.getType().toString());
            }
            rules.add(new DTORule(rule.getName(), rule.getActivation().getTicks(), rule.getActivation().getProbability(), actionNames));
        }

        DTOTermination termination = new DTOTermination(
                Optional.ofNullable(simulation.getTermination().getBySecond()).map(o -> o.getCount()).orElse(null),
                Optional.ofNullable(simulation.getTermination().getByTicks()).map(o -> (int) o.getCount()).orElse(null));
        return new DTOSimulationDetails(getEntities(simulation), rules, termination);
    }

    private void archiveSimulation() {
        pastSimulations.put(simulation.getId(), simulation);
        try {
            simulation = new Simulation(getWorldFromFile(filepath));
        } catch (FileNotFoundException | JAXBException | InvalidClassException e) {
            throw new RuntimeException(e);
        }
        idCounter++;
    }

    private void isSimulationLoaded() throws NullPointerException {
        if (simulation == null) {
            throw new NullPointerException("No simulation is loaded.");
        }
    }

    @Override
    public void saveToFile(String filepath) {
        try (ObjectOutputStream encoder = new ObjectOutputStream(new FileOutputStream(filepath.concat(".xml")))){
            encoder.writeObject(this);
            encoder.flush();
        } catch (Exception e) {
            throw new RuntimeException("Error saving to file: " + e.getMessage());
        }
    }

    @Override
    public void loadFromFile(String filepath) {
        try (ObjectInputStream decoder = new ObjectInputStream(new FileInputStream(filepath.concat(".xml")))){
            Engine engine = (Engine) decoder.readObject();
            this.pastSimulations.putAll(engine.pastSimulations);
            this.simulation = engine.simulation;
            this.idCounter = engine.idCounter;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error loading from file: " + e.getMessage());
        }
    }
}

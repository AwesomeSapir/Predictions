import dto.detail.*;
import dto.simulation.*;
import javafx.util.Pair;
import validator.Validator;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main implements UIFunctions {

    private final Scanner scanner = new Scanner(System.in);
    private final EngineInterface engine = new Engine();

    public static void main(String[] args) {
        Main mainClass = new Main();

        List<String> menu = new ArrayList<>();
        menu.add("Load XML File");
        menu.add("Display Simulation Details");
        menu.add("Run Simulation");
        menu.add("Display Past Runs");
        menu.add("Exit");
        menu.add("Save");
        menu.add("Load");

        while (true) {
            for (int i = 0; i < menu.size(); i++) {
                System.out.println((i + 1) + ". " + menu.get(i));
            }
            int selection = mainClass.getSelectionForCollection(menu, "Select an option:") + 1;
            switch (selection){
                case 1:
                    mainClass.loadFile();
                    break;
                case 2:
                    mainClass.showSimulationDetails();
                    break;
                case 3:
                    mainClass.runSimulation();
                    break;
                case 4:
                    mainClass.showPastSimulation();
                    break;
                case 5:
                    mainClass.exit();
                    break;
                case 6:
                    mainClass.save();
                    break;
                case 7:
                    mainClass.load();
                    break;
            }
        }

    }


    @Override
    public void loadFile() {
        System.out.print("Enter the full path to the XML file: ");
        String filepath = scanner.nextLine();
        try {
            engine.loadXml(filepath);
            System.out.println("The file was loaded successfully.");
        } catch (Exception e) {
            System.out.print("The file isn't valid:");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void showSimulationDetails() throws NullPointerException {
        DTOSimulationDetails simulationDetails = engine.getSimulationDetails();
        printSimulationDetails(simulationDetails);
    }

    public void printSimulationDetails(DTOSimulationDetails simulationDetails) throws NullPointerException {
        System.out.println("Entities:");
        for (DTOEntity entity : simulationDetails.getEntities()) {
            System.out.println("Entity Name: " + entity.getName());
            System.out.println("Population: " + entity.getPopulation());

            System.out.println("Properties:");
            List<DTOProperty> properties = new ArrayList<>(entity.getProperties());
            for (DTOProperty property : properties) {
                System.out.println("  - Property Name: " + property.getName());
                System.out.println("    Type: " + property.getType());
                if (property.getRange() != null) {
                    System.out.println("    Range: (" + property.getRange().getFrom() + ", " + property.getRange().getTo() + ")");
                }
                System.out.println("    Randomly Initialized: " + property.isRandomInit());
            }
            System.out.println();
        }

        System.out.println("Rules:");
        for (DTORule rule : simulationDetails.getRules()) {
            System.out.println("Rule Name: " + rule.getName());
            System.out.println("Activation Ticks: " + rule.getTicks());
            System.out.println("Activation Probability: " + rule.getProbability());
            System.out.println("Number of Actions: " + rule.getActions().size());
            System.out.println("Actions:");
            for (String action : rule.getActions()) {
                System.out.println("  - " + action);
            }
            System.out.println();
        }

        DTOTermination termination = simulationDetails.getTermination();

        System.out.println("Termination Conditions:");
        if (termination.getTicks() != null) {
            System.out.println("By Ticks: " + termination.getTicks());
        }
        if (termination.getSeconds() != null) {
            System.out.println("By Seconds: " + termination.getSeconds());
        }
    }

    @Override
    public void runSimulation() throws NullPointerException {
        List<DTOEnvironmentVariable> environmentVariables = new ArrayList<>(engine.getEnvironmentDefinitions());
        printEnvDefinitions(environmentVariables);
        List<Pair<String, Object>> newValues = new ArrayList<>();
        boolean exit = environmentVariables.isEmpty();
        while (!exit) {
            System.out.println("Select 0 to continue or an environment variable to initialize:");
            String input = scanner.nextLine();
            if (Validator.validate(input).isInteger().isInRange(0, environmentVariables.size()).isValid()) {
                int selection = Integer.parseInt(input);
                if (selection == 0) {
                    exit = true;
                } else {
                    DTOEnvironmentVariable environmentVariable = environmentVariables.get(selection - 1);
                    String type = environmentVariable.getType();
                    System.out.println("Enter a value for the variable (" + type + "):");
                    String strValue = scanner.nextLine();
                    Object value = null;
                    Validator validator = Validator.validate(strValue);
                    switch (type) {
                        case "DECIMAL":
                            if (validator.isInteger().isInRange(environmentVariable.getRange().getFrom(), environmentVariable.getRange().getTo()).isValid()) {
                                value = Integer.parseInt(strValue);
                            }
                            break;
                        case "FLOAT":
                            if (validator.isDouble().isInRange(environmentVariable.getRange().getFrom(), environmentVariable.getRange().getTo()).isValid()) {
                                value = Double.parseDouble(strValue);
                            }
                            break;
                        case "STRING":
                            if (validator.isValidString().isValid()) {
                                value = strValue;
                            }
                            break;
                        case "BOOLEAN":
                            if (validator.isBoolean().isValid()) {
                                value = Boolean.parseBoolean(strValue);
                            }
                            break;
                    }
                    if (value != null) {
                        newValues.add(new Pair<>(environmentVariable.getName(), value));
                    } else {
                        System.out.println("The new value is invalid.");
                    }
                }
            } else {
                System.out.println("Invalid selection.");
            }
        }
        engine.setEnvironmentValues(newValues);

        printEnvValues(engine.getEnvironmentValues());

        System.out.println("Running simulation...");
        DTOSimulationResult simulationResult = engine.runSimulation();
        System.out.println("Simulation finished.");
        printSimulationResult(simulationResult);
    }

    public void printEnvValues(Collection<DTOEnvironmentVariable> environmentVariables) {
        int index = 1;
        System.out.println("Environment Values:");
        for (DTOEnvironmentVariable environmentVariable : environmentVariables) {
            String name = environmentVariable.getName();
            String value = environmentVariable.getValue().toString();

            System.out.println(index + ". Environment variable: " + name);
            System.out.println("Value: " + value);
            index++;
        }
    }

    public void printEnvDefinitions(Collection<DTOEnvironmentVariable> environmentVariables) {
        int index = 1;
        System.out.println("Environment Variables:");
        for (DTOEnvironmentVariable environmentVariable : environmentVariables) {
            String name = environmentVariable.getName();
            String type = environmentVariable.getType();

            System.out.println(index + ". Environment variable: " + name);
            System.out.println("Type: " + type);
            System.out.println("Range: (" + environmentVariable.getRange().getFrom() + ", " + environmentVariable.getRange().getTo() + ")");
            index++;
        }
    }

    public void printSimulationResult(DTOSimulationResult simulationResult) {
        System.out.println("Simulation result:");
        System.out.println("Id: " + simulationResult.getId());
        String termination = "";
        if (simulationResult.isBySeconds()) {
            termination += " by seconds";
        }
        if (simulationResult.isByTicks()) {
            termination += " by ticks";
        }
        System.out.println("Termination reason:" + termination);
    }

    @Override
    public void showPastSimulation() {
        System.out.println("Past Simulations:");
        List<DTOSimulation> pastSimulations = new ArrayList<>(engine.getPastSimulations());
        printPastSimulations(pastSimulations);

        DTOSimulation simulation = pastSimulations.get(getSelectionForCollection(pastSimulations, "Select a simulation to display:"));
        List<String> detailTypes = new ArrayList<>();
        detailTypes.add("Amount of entities");
        detailTypes.add("Property value histogram");
        for (int i = 0; i<detailTypes.size(); i++){
            System.out.println((i+1) + ". " + detailTypes.get(i));
        }

        switch (getSelectionForCollection(detailTypes, "Select display type:")){
            case 0:
                printEntityPopulation(engine.getDetailsByEntityCount(simulation.getId()));
                break;
            case 1:
                List<DTOEntity> entities = new ArrayList<>(engine.getPastEntities(simulation.getId()));
                int entityIndex = getSelectionForCollection(entities, "Select an entity:");
                DTOEntity entity = entities.get(entityIndex);
                List<DTOProperty> properties = new ArrayList<>(engine.getPastEntityProperties(simulation.getId(), entity.getName()));
                int propertyIndex = getSelectionForCollection(properties, "Select a property:");
                DTOProperty property = properties.get(propertyIndex);
                DTOSimulationHistogram histogram = engine.getValuesForPropertyHistogram(simulation.getId(), property.getName());
                printSimulationHistogram(histogram);
                break;
        }
    }

    public void printSimulationHistogram(DTOSimulationHistogram histogram){
        System.out.println("Histogram for " + histogram.getPropertyName() + ":");
        for (Map.Entry<Object, Integer> entry : histogram.getValueToCount().entrySet()){
            System.out.println(entry.getValue() + " instances where " + histogram.getPropertyName() + " is " + entry.getKey());
        }
    }

    public void printEntityPopulation(Collection<DTOEntityPopulation> entityPopulations){
        for (DTOEntityPopulation entityPopulation : entityPopulations){
            System.out.println("Entity Name: " + entityPopulation.getEntity().getName());
            System.out.println("Initial Quantity: " + entityPopulation.getInitialPopulation());
            System.out.println("Final Quantity: " + entityPopulation.getFinalPopulation());
            System.out.println();
        }
    }

    public int getSelectionForCollection(Collection<?> collection, String prompt){
        boolean exit = collection.isEmpty();
        int selection = -1;
        while (!exit) {
            System.out.println(prompt);
            String input = scanner.nextLine();
            if (Validator.validate(input).isInteger().isInRange(1, collection.size()).isValid()) {
                selection = Integer.parseInt(input);
                exit = true;
            } else {
                System.out.println("Invalid selection.");
            }
        }
        return selection - 1;
    }

    public void printPastSimulations(Collection<DTOSimulation> pastSimulations){
        int runIndex = 1;
        for (DTOSimulation simulation : pastSimulations) {
            System.out.println(runIndex + ". Running date: " + simulation.getBeginTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy | HH.mm.ss")));
            System.out.println("   Unique identifier: " + simulation.getId());
            runIndex++;
        }
    }

    @Override
    public void exit() {

    }

    @Override
    public void save() {

    }

    @Override
    public void load() {

    }
}
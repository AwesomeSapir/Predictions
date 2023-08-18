package ui;

import dto.detail.*;
import dto.simulation.*;
import engine.Engine;
import engine.EngineInterface;
import javafx.util.Pair;
import validator.Validator;

import java.time.format.DateTimeFormatter;
import java.util.*;

public class ConsoleUI implements MainUI{

    private final Scanner scanner = new Scanner(System.in);
    private final EngineInterface engine = new Engine();

    public void loadFile() {
        System.out.println();
        System.out.print("Enter the full path to the XML file: ");
        String filepath = scanner.nextLine();
        try {
            engine.loadXml(filepath);
            System.out.println();
            System.out.println("The file was loaded successfully.");
        } catch (Exception e) {
            System.out.println();
            System.out.println("The file isn't valid: " + e.getMessage());
        }
    }

    public void showSimulationDetails() throws NullPointerException {
        DTOSimulationDetails simulationDetails = engine.getSimulationDetails();
        printSimulationDetails(simulationDetails);
    }

    public void printSimulationDetails(DTOSimulationDetails simulationDetails) throws NullPointerException {
        System.out.println();
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
            System.out.println("Number of Actions: " + rule.getActionsAmount());
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

    public void runSimulation() throws NullPointerException {
        List<DTOEnvironmentVariable> environmentVariables = new ArrayList<>(engine.getEnvironmentDefinitions());
        printEnvDefinitions(environmentVariables);
        List<Pair<String, Object>> newValues = new ArrayList<>();
        boolean exit = environmentVariables.isEmpty();
        while (!exit) {
            System.out.println();
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
                        System.out.println();
                        System.out.println("The new value is invalid.");
                    }
                }
            } else {
                System.out.println();
                System.out.println("Invalid selection.");
            }
        }
        engine.setEnvironmentValues(newValues);

        printEnvValues(engine.getEnvironmentValues());

        System.out.println();
        System.out.println("Running simulation...");
        DTOSimulationResult simulationResult = engine.runSimulation();
        System.out.println();
        System.out.println();
        System.out.println("Simulation finished.");
        printSimulationResult(simulationResult);
    }

    public void printEnvValues(Collection<DTOEnvironmentVariable> environmentVariables) {
        int index = 1;
        System.out.println();
        System.out.println("Environment Values:");
        for (DTOEnvironmentVariable environmentVariable : environmentVariables) {
            String name = environmentVariable.getName();
            Object value = environmentVariable.getValue();

            System.out.println(index + ". Environment variable: " + name);
            System.out.print("Value: ");
            printObjectByClass(value);
            index++;
        }
    }

    public void printObjectByClass(Object value){
        if(value instanceof String){
            System.out.println("\"" + value +"\"");
        }
        else{
            System.out.println(value);
        }
    }
    public void printEnvDefinitions(Collection<DTOEnvironmentVariable> environmentVariables) {
        int index = 1;
        System.out.println();
        System.out.println("Environment Variables:");
        for (DTOEnvironmentVariable environmentVariable : environmentVariables) {
            String name = environmentVariable.getName();
            String type = environmentVariable.getType();

            System.out.println(index + ". Environment variable: " + name);
            System.out.println("Type: " + type);
            if(environmentVariable.getRange() != null) {
                System.out.println("Range: (" + environmentVariable.getRange().getFrom() + ", " + environmentVariable.getRange().getTo() + ")");
            } else {
                System.out.println("Range: N/A");
            }
            index++;
        }
    }

    public void printSimulationResult(DTOSimulationResult simulationResult) {
        System.out.println();
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

    public void showPastSimulation() {
        System.out.println();
        System.out.println("Past Simulations:");
        List<DTOSimulation> pastSimulations = new ArrayList<>(engine.getPastSimulations());
        if(pastSimulations.isEmpty()){
            System.out.println("None found.");
            return;
        }
        printPastSimulations(pastSimulations);

        System.out.println();
        DTOSimulation simulation = pastSimulations.get(getSelectionForCollection(pastSimulations, "Select a simulation to display:"));
        List<String> detailTypes = new ArrayList<>();
        detailTypes.add("Amount of entities");
        detailTypes.add("Property value histogram");
        System.out.println();
        for (int i = 0; i<detailTypes.size(); i++){
            System.out.println((i+1) + ". " + detailTypes.get(i));
        }

        System.out.println();
        switch (getSelectionForCollection(detailTypes, "Select display type:")){
            case 0:
                System.out.println();
                printEntityPopulation(engine.getDetailsByEntityCount(simulation.getId()));
                break;
            case 1:
                System.out.println();
                List<DTOEntity> entities = new ArrayList<>(engine.getPastEntities(simulation.getId()));
                for (int i = 0; i < entities.size(); i++) {
                    System.out.println((i+1) + ". " + entities.get(i).getName());
                }
                System.out.println();
                int entityIndex = getSelectionForCollection(entities, "Select an entity:");
                DTOEntity entity = entities.get(entityIndex);
                System.out.println();
                List<DTOProperty> properties = new ArrayList<>(engine.getPastEntityProperties(simulation.getId(), entity.getName()));
                for (int i = 0; i < properties.size(); i++) {
                    System.out.println((i+1) + ". " + properties.get(i).getName());
                }
                System.out.println();
                int propertyIndex = getSelectionForCollection(properties, "Select a property:");
                DTOProperty property = properties.get(propertyIndex);
                DTOSimulationHistogram histogram = engine.getValuesForPropertyHistogram(simulation.getId(), property.getName());
                System.out.println();
                printSimulationHistogram(histogram);
                break;
        }
    }

    public void printSimulationHistogram(DTOSimulationHistogram histogram){
        System.out.println("Histogram for " + histogram.getPropertyName() + ":");
        if(histogram.getValueToCount().isEmpty()){
            System.out.println("No entities left with this property.");
        }
        for (Map.Entry<Object, Integer> entry : histogram.getValueToCount().entrySet()){
            System.out.print(entry.getValue() + " instances where " + histogram.getPropertyName() + " is ");
            printObjectByClass(entry.getKey());
        }
    }

    public void printEntityPopulation(Collection<DTOEntityPopulation> entityPopulations){
        for (DTOEntityPopulation entityPopulation : entityPopulations){
            System.out.println("Entity Name: " + entityPopulation.getEntity().getName());
            System.out.println("Initial Quantity: " + entityPopulation.getInitialPopulation());
            System.out.println("Final Quantity: " + entityPopulation.getFinalPopulation());
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
                System.out.println();
                System.out.println("Invalid selection.");
                System.out.println();
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

    public void exit() {
        System.out.println("System is exiting.");
        scanner.close();
        System.exit(0);
    }

    public void save() {
        System.out.print("Enter the path of the file you want save to: ");
        String filepath = scanner.nextLine();
        try {
            engine.saveToFile(filepath);
            System.out.println("System was saved to file successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void load() {
        System.out.print("Enter the path of the file you saved to: ");
        String filepath = scanner.nextLine();
        try {
            engine.loadFromFile(filepath);
            System.out.println("System was loaded from file successfully.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        List<String> menu = new ArrayList<>();
        menu.add("Load XML File");
        menu.add("Display Simulation Details");
        menu.add("Run Simulation");
        menu.add("Display Past Runs");
        menu.add("Exit");
        menu.add("Save");
        menu.add("Load");

        while (true) {
            System.out.println();
            System.out.println();
            for (int i = 0; i < menu.size(); i++) {
                System.out.println((i + 1) + ". " + menu.get(i));
            }
            System.out.println();
            try {
                int selection = getSelectionForCollection(menu, "Select an option:") + 1;
                switch (selection){
                    case 1:
                        loadFile();
                        break;
                    case 2:
                        showSimulationDetails();
                        break;
                    case 3:
                        runSimulation();
                        break;
                    case 4:
                        showPastSimulation();
                        break;
                    case 5:
                        exit();
                        break;
                    case 6:
                        save();
                        break;
                    case 7:
                        load();
                        break;
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

}

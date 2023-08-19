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
    private final MenuHelper menuHelper;

    public ConsoleUI() {
        menuHelper = new MenuHelper(scanner);
    }

    public void loadFile() {
        System.out.print("Enter the full path to the XML file: ");
        String filepath = scanner.nextLine();
        try {
            engine.loadXml(filepath);
            System.out.println("The file was loaded successfully.");
        } catch (Exception e) {
            System.out.println("The file isn't valid: " + e.getMessage());
        }
    }

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
        menuHelper.printMenu(environmentVariables,
                "Environment Variables",
                object -> {
            String result = "";
            DTOEnvironmentVariable environmentVariable = (DTOEnvironmentVariable) object;
            result += "Variable name: " + environmentVariable.getName();
            result += "Type: " + environmentVariable.getType();
            if(environmentVariable.getRange() != null) {
                result += "Range: (" + environmentVariable.getRange().getFrom() + ", " + environmentVariable.getRange().getTo() + ")";
            } else {
                result += "Range: N/A";
            }
            return result;
        });
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

        menuHelper.printMenu(engine.getEnvironmentValues(),
                "Environment Values",
                object ->
                        "Environment variable: " + ((DTOEnvironmentVariable)object).getName() +
                                "\nValue: " + ((DTOEnvironmentVariable)object).getValue());

        System.out.println("Running simulation...");
        DTOSimulationResult simulationResult = engine.runSimulation();
        System.out.println("Simulation finished.");
        printSimulationResult(simulationResult);
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

    public void showPastSimulation() {
        System.out.println("Past Simulations:");
        List<DTOSimulation> pastSimulations = new ArrayList<>(engine.getPastSimulations());
        menuHelper.printMenu(pastSimulations,
                "Select a simulation to display:",
                object -> "Running date: " + ((DTOSimulation)object).getBeginTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy | HH.mm.ss")) + "\n   Unique identifier: " + ((DTOSimulation)object).getId());

        DTOSimulation simulation = pastSimulations.get(menuHelper.getSelectionForCollection(pastSimulations, "Select a simulation to display:"));
        List<String> detailTypes = Arrays.asList(
                "Amount of entities",
                "Property value histogram");
        menuHelper.printMenu(detailTypes,
                Object::toString);

        switch (menuHelper.getSelectionForCollection(detailTypes, "Select display type:")){
            case 0:
                printEntityPopulation(engine.getDetailsByEntityCount(simulation.getId()));
                break;
            case 1:
                List<DTOEntity> entities = new ArrayList<>(engine.getPastEntities(simulation.getId()));
                menuHelper.printMenu(entities,
                        "Entities:",
                        object -> ((DTOEntity)object).getName());
                int entityIndex = menuHelper.getSelectionForCollection(entities, "Select an entity:");

                DTOEntity entity = entities.get(entityIndex);
                List<DTOProperty> properties = new ArrayList<>(engine.getPastEntityProperties(simulation.getId(), entity.getName()));
                menuHelper.printMenu(properties,
                        "Properties:",
                        object -> ((DTOProperty)object).getName());
                int propertyIndex = menuHelper.getSelectionForCollection(properties, "Select a property:");

                DTOProperty property = properties.get(propertyIndex);
                DTOSimulationHistogram histogram = engine.getValuesForPropertyHistogram(simulation.getId(), property.getName());
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
            System.out.println(entry.getValue() + " instances where " + histogram.getPropertyName() + " is " + entry.getKey());
        }
    }

    public void printEntityPopulation(Collection<DTOEntityPopulation> entityPopulations){
        for (DTOEntityPopulation entityPopulation : entityPopulations){
            System.out.println("Entity Name: " + entityPopulation.getEntity().getName());
            System.out.println("Initial Quantity: " + entityPopulation.getInitialPopulation());
            System.out.println("Final Quantity: " + entityPopulation.getFinalPopulation());
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
            menuHelper.printMenu(menu, "Main Menu", Object::toString);

            try {
                int selection = menuHelper.getSelectionForCollection(menu, "Select an option:") + 1;
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
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

}

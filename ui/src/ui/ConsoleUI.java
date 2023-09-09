package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ui.component.main.MainController;
import ui.engine.EngineManager;
import ui.style.StyleManager;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ConsoleUI extends Application implements MainUI{

    private EngineManager engineManager = new EngineManager();

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Predictions");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ui/component/main/mainScreen.fxml"));
        Parent root = loader.load();

        MainController mainController = loader.getController();
        mainController.setEngineManager(engineManager);

        Scene scene = new Scene(root, 600, 400);
        loadFonts();
        StyleManager.register(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(400);
    }

    private void loadFonts(){
        List<String> fonts = Arrays.asList(
                "Roboto-Regular",
                "Roboto-Bold",
                "Poppins-Bold",
                "Poppins-Medium");
        for (String font : fonts){
            Font.loadFont(Objects.requireNonNull(getClass().getResource("/ui/resources/font/" + font + ".ttf")).toExternalForm(), 24);
        }
    }

    /*
    public void loadFile() {
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
        System.out.println();
        menuHelper.printMenu(environmentVariables,
                "Environment Variables",
                object -> {
            String result = "";
            DTOEnvironmentVariable environmentVariable = (DTOEnvironmentVariable) object;
            result += "Variable name: " + environmentVariable.getName();
            result += "\n   Type: " + environmentVariable.getType();
            if(environmentVariable.getRange() != null) {
                result += "\n   Range: (" + environmentVariable.getRange().getFrom() + ", " + environmentVariable.getRange().getTo() + ")";
            } else {
                result += "\n   Range: N/A";
            }
            return result;
        });
        List<Pair<String, Object>> newValues = new ArrayList<>();
        boolean exit = environmentVariables.isEmpty();
        while (!exit) {
            System.out.println();
            System.out.print("Select 0 to continue or an environment variable to initialize: ");
            String input = scanner.nextLine();
            if (Validator.validate(input).isInteger().isInRange(0, environmentVariables.size()).isValid()) {
                int selection = Integer.parseInt(input);
                if (selection == 0) {
                    exit = true;
                } else {
                    DTOEnvironmentVariable environmentVariable = environmentVariables.get(selection - 1);
                    String type = environmentVariable.getType();
                    System.out.print("Enter a value for the variable (" + type + "): ");
                    Object value = null;
                    if(type.equals("BOOLEAN")){
                        System.out.println();
                        List<Boolean> booleans = Arrays.asList(true, false);
                        menuHelper.printMenu(booleans, Object::toString);
                        value = booleans.get(menuHelper.getSelectionForCollection(booleans, "Select a boolean value"));
                    } else {
                        String strValue = scanner.nextLine();
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
                        }
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

        System.out.println();
        menuHelper.printMenu(engine.getEnvironmentValues(),
                "Environment Values",
                object ->
                        "Environment variable: " + ((DTOEnvironmentVariable)object).getName() +
                                "\n   Value: " + formatValueByClass(((DTOEnvironmentVariable)object).getValue()));

        System.out.println();
        System.out.println("Running simulation...");
        DTOSimulationResult simulationResult = engine.runSimulation();
        System.out.println();
        System.out.println("Simulation finished.");
        printSimulationResult(simulationResult);
    }

    public String formatValueByClass(Object value){
        String result = "";
        if(value instanceof String){
            result += "\"" + value +"\"";
        }
        else{
            result += value.toString();
        }
        return result;
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
        List<DTOSimulation> pastSimulations = new ArrayList<>(engine.getPastSimulations());
        menuHelper.printMenu(pastSimulations,
                "Past Simulations",
                object -> "Running date: " + ((DTOSimulation)object).getBeginTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy | HH.mm.ss")) + "\n   Unique identifier: " + ((DTOSimulation)object).getId());

        int pastSimulationIndex = menuHelper.getSelectionForCollection(pastSimulations, "Select a simulation to display");
        if(pastSimulationIndex < 0){
            return;
        }
        DTOSimulation simulation = pastSimulations.get(pastSimulationIndex);
        List<String> detailTypes = Arrays.asList(
                "Amount of entities",
                "Property value histogram");

        System.out.println();
        menuHelper.printMenu(detailTypes,
                "Display Types",
                Object::toString);

        System.out.println();
        switch (menuHelper.getSelectionForCollection(detailTypes, "Select display type")){
            case 0:
                System.out.println();
                printEntityPopulation(engine.getDetailsByEntityCount(simulation.getId()));
                break;
            case 1:
                System.out.println();
                List<DTOEntity> entities = new ArrayList<>(engine.getPastEntities(simulation.getId()));
                menuHelper.printMenu(entities,
                        "Entities",
                        object -> ((DTOEntity)object).getName());
                System.out.println();
                int entityIndex = menuHelper.getSelectionForCollection(entities, "Select an entity");
                if(entityIndex < 0){
                    return;
                }
                DTOEntity entity = entities.get(entityIndex);
                List<DTOProperty> properties = new ArrayList<>(engine.getPastEntityProperties(simulation.getId(), entity.getName()));
                System.out.println();
                menuHelper.printMenu(properties,
                        "Properties",
                        object -> ((DTOProperty)object).getName());
                System.out.println();
                int propertyIndex = menuHelper.getSelectionForCollection(properties, "Select a property");
                if(propertyIndex < 0){
                    return;
                }
                System.out.println();
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
            System.out.println(entry.getValue() + " instances where " + histogram.getPropertyName() + " is " + formatValueByClass(entry.getKey()));
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
            System.out.println();
            System.out.println("System was loaded from file successfully.");
        } catch (Exception e) {
            System.out.println();
            System.out.println(e.getMessage());
        }
    }
    */


    @Override
    public void run(String[] args) {
        launch(args);
    }
}

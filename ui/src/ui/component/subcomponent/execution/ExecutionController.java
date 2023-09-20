package ui.component.subcomponent.execution;

import dto.detail.DTOEntity;
import dto.detail.DTOEnvironmentVariable;
import dto.detail.DTOObject;
import dto.simulation.DTOSimulationDetails;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.Pair;
import ui.Notify;
import ui.component.custom.input.generic.InputItemView;
import ui.component.custom.input.simulation.EntityPopulationView;
import ui.component.custom.input.simulation.EnvironmentVariableView;
import ui.component.main.MainController;
import ui.engine.EngineManager;
import ui.engine.Simulation;
import ui.style.Animations;

import java.util.*;

public class ExecutionController {
    @FXML
    public VBox vboxEntityPopulation;
    @FXML
    public VBox vboxEnvVariables;
    @FXML
    public Button buttonClear;
    @FXML
    public Button buttonStart;

    private MainController mainController; // Add a reference to MainController

    private EngineManager engineManager;

    private final IntegerProperty populationMax = new SimpleIntegerProperty(0);
    private final IntegerProperty populationCurrent = new SimpleIntegerProperty(0);

    @FXML
    public void initialize() {
        buttonClear.setOnMouseClicked(this::clearClicked);
    }

    public void setEngineManager(EngineManager engineManager) {
        this.engineManager = engineManager;
        engineManager.isSimulationLoadedProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("simulation loaded changed to: " + newValue);
            if (newValue) {
                DTOSimulationDetails simulationDetails = engineManager.getSimulationDetails();
                populationMax.setValue(simulationDetails.getGrid().getRows() * simulationDetails.getGrid().getCols());
                populateVBox(vboxEntityPopulation, simulationDetails.getEntities());
                populateVBox(vboxEnvVariables, engineManager.getEnvironmentDefinitions());
            }
        });
    }

    public void populateVBox(VBox vBox, Collection<? extends DTOObject> collection) {
        vBox.getChildren().clear();
        for (DTOObject dtoObject : collection) {
            Separator separator = new Separator(Orientation.HORIZONTAL);
            VBox.setMargin(separator, new Insets(0, -16, 0, -16));
            InputItemView<?> inputItemView;
            if (dtoObject instanceof DTOEntity) {
                EntityPopulationView populationView = new EntityPopulationView((DTOEntity) dtoObject, populationMax.get());
                inputItemView = populationView;
            } else if (dtoObject instanceof DTOEnvironmentVariable) {
                inputItemView = EnvironmentVariableView.create((DTOEnvironmentVariable) dtoObject);
            } else {
                throw new RuntimeException("Invalid DTOObject " + dtoObject.getClass().getName());
            }
            vBox.getChildren().addAll(
                    inputItemView,
                    separator);
        }
        vBox.getChildren().remove(vBox.getChildren().size() - 1);
    }

    // Add a method to set the reference
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void clearClicked(MouseEvent mouseEvent) {
        clearInputItems(vboxEntityPopulation);
        clearInputItems(vboxEnvVariables);
        Notify.getInstance().showAlertBar("Cleared.");
    }

    private void clearInputItems(VBox container) {
        for (Node view : container.getChildren()) {
            if (view instanceof InputItemView<?>) {
                ((InputItemView<?>) view).clear();
            }
        }
    }

    public void startClicked(MouseEvent mouseEvent) {
        String errorMessage = "";
        boolean isPopulationValid = checkEntityPopulation(vboxEntityPopulation);
        boolean isEnvVariablesValid = checkForValidItems(vboxEnvVariables);

        if (!isPopulationValid) {
            errorMessage += "The population exceeds the maximum amount " + populationMax + ".";
        }
        if (!isEnvVariablesValid) {
            errorMessage += "There are invalid items on the form.";
        }

        if (isPopulationValid && isEnvVariablesValid) {
            Notify.getInstance().showAlertDialog("Success", null, "The simulation is loaded.", Alert.AlertType.CONFIRMATION);

            engineManager.runSimulation(getEntityPopulations(), getEnvironmentValues());
            mainController.switchToResultsTab();
        } else {
            Notify.getInstance().showAlertDialog("Invalid input!", null, errorMessage, Alert.AlertType.ERROR);
        }
    }

    public void restoreValues(int id){
        Notify.getInstance().showAlertBar("Simulation #" + id + " details restored.");
        Simulation simulation = engineManager.getSimulations().get(id);
        Map<String, Integer> populations = simulation.getInitialPopulations();
        Map<String, Object> envValues = simulation.getInitialEnvValues();

        for (Node view : vboxEntityPopulation.getChildren()){
            if (view instanceof EntityPopulationView) {
                ((EntityPopulationView) view).setValue((populations.get(((EntityPopulationView) view).getTitle()).doubleValue()));
            }
        }

        for (Node view : vboxEnvVariables.getChildren()){
            if (view instanceof InputItemView<?>) {
                ((InputItemView<?>) view).setValue(envValues.get(((InputItemView<?>) view).getTitle()));
            }
        }
    }

    private Map<String, Integer> getEntityPopulations() {
        Map<String, Integer> result = new LinkedHashMap<>();
        for (Node view : vboxEntityPopulation.getChildren()) {
            if (view instanceof InputItemView<?>) {
                Pair<String, Object> pair = getValue(view);
                result.put(pair.getKey(), ((Double) pair.getValue()).intValue());
            }
        }
        return result;
    }

    private Map<String, Object> getEnvironmentValues() {
        Map<String, Object> result = new LinkedHashMap<>();
        for (Node view : vboxEnvVariables.getChildren()) {
            if (view instanceof InputItemView<?>) {
                Pair<String, Object> pair = getValue(view);
                result.put(pair.getKey(), pair.getValue());
            }
        }
        return result;
    }

    private Pair<String, Object> getValue(Node view) {
        return new Pair<>(((InputItemView<?>) view).getTitle(), ((InputItemView<?>) view).getValue());
    }

    private boolean checkEntityPopulation(VBox container) {
        int currentPopulation = 0;
        for (Node view : container.getChildren()) {
            if (view instanceof EntityPopulationView) {
                currentPopulation += ((EntityPopulationView) view).getValue();
            }
        }
        return currentPopulation <= populationMax.get();
    }

    private boolean checkForValidItems(VBox container) {
        for (Node view : container.getChildren()) {
            if (view instanceof InputItemView<?>) {
                if (!((InputItemView<?>) view).isValid()) {
                    return false;
                }
            }
        }
        return true;
    }

    private Timeline buttonStartAniamtion = new Timeline(new KeyFrame(Duration.millis(10000), event -> {
        Animations.highlight(buttonStart, 2);
    }));

    public void onFocus() {
        buttonStartAniamtion.playFromStart();
    }

    public void onUnfocused() {
        buttonStartAniamtion.stop();
    }
}

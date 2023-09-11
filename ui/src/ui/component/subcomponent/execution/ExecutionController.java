package ui.component.subcomponent.execution;

import dto.detail.DTOEntity;
import dto.detail.DTOEnvironmentVariable;
import dto.detail.DTOObject;
import dto.simulation.DTOSimulationDetails;
import javafx.application.Platform;
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
import javafx.util.Pair;
import ui.component.custom.input.generic.InputItemView;
import ui.component.custom.input.simulation.EntityPopulationView;
import ui.component.custom.input.simulation.EnvironmentVariableView;
import ui.component.main.MainController;
import ui.engine.EngineManager;
import ui.style.Animations;
import ui.style.StyleManager;

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
                populationMax.setValue(simulationDetails.getSpaceSize());
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
            showAlert("Success", "The simulation is loaded.", Alert.AlertType.CONFIRMATION);

            engineManager.setEntityPopulations(getEntityPopulations());
            engineManager.setEnvironmentValues(getEnvironmentValues());

            engineManager.runSimulation();

            mainController.switchToResultsTab();
        } else {
            showAlert("Invalid input!", errorMessage, Alert.AlertType.ERROR);
        }
    }

    private Collection<Pair<String, Integer>> getEntityPopulations() {
        List<Pair<String, Integer>> result = new ArrayList<>();
        for (Node view : vboxEntityPopulation.getChildren()) {
            if (view instanceof InputItemView<?>) {
                Pair<String, Object> pair = getValue(view);
                result.add(new Pair<>(pair.getKey(), ((Double) pair.getValue()).intValue()));
            }
        }
        return result;
    }

    private Collection<Pair<String, Object>> getEnvironmentValues() {
        List<Pair<String, Object>> result = new ArrayList<>();
        for (Node view : vboxEnvVariables.getChildren()) {
            if (view instanceof InputItemView<?>) {
                result.add(getValue(view));
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

    private void showAlert(String title, String contentText, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        StyleManager.register(alert.getDialogPane().getScene());
        alert.setOnCloseRequest(event -> StyleManager.unregister(alert.getDialogPane().getScene()));
        alert.showAndWait();
    }

    private Timer timer;

    public void onFocus() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Animations.highlight(buttonStart, 2);
                });
            }
        }, 500, 5000);
    }

    public void onUnfocused() {
        timer.cancel();
    }
}

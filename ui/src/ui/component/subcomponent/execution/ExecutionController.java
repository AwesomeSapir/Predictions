package ui.component.subcomponent.execution;

import dto.detail.DTOEntity;
import dto.detail.DTOEnvironmentVariable;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import ui.component.custom.input.simulation.EntityPopulationView;
import ui.component.custom.input.simulation.EnvironmentVariableView;
import ui.component.custom.input.generic.InputItemView;
import ui.component.custom.input.generic.TextInputItemView;
import ui.engine.EngineManager;
import ui.component.main.MainController;

import java.util.ArrayList;
import java.util.List;

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

    @FXML
    public void initialize() {
        buttonClear.setOnMouseClicked(this::clearClicked);
    }

    public void setEngineManager(EngineManager engineManager) {
        this.engineManager = engineManager;
        engineManager.isSimulationLoadedProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("simulation loaded changed to: " + newValue);
            if (newValue) {
                populateEntities();
                populateEnvVariables();
            }
        });
    }

    public void populateEntities() {
        vboxEntityPopulation.getChildren().clear();
        for (DTOEntity entity : engineManager.getSimulationDetails().getEntities()) {
            vboxEntityPopulation.getChildren().addAll(
                    new EntityPopulationView(entity),
                    new Separator(Orientation.HORIZONTAL));
        }
    }

    public void populateEnvVariables() {
        vboxEnvVariables.getChildren().clear();
        for (DTOEnvironmentVariable environmentVariable : engineManager.getEnvironmentDefinitions()) {
            vboxEnvVariables.getChildren().addAll(
                    new EnvironmentVariableView(environmentVariable).getView(),
                    new Separator(Orientation.HORIZONTAL));
        }
    }

    // Add a method to set the reference
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void clearClicked(MouseEvent mouseEvent) {
        System.out.println("Clear clicked");
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
        System.out.println("Start clicked");

        boolean hasInvalidItem = checkForInvalidItems(vboxEntityPopulation) || checkForInvalidItems(vboxEnvVariables);

        if (hasInvalidItem) {
            showErrorAlert("Invalid Items", "There are invalid items on the form.");
        } else {
            showConfirmationAlert("Success", "The simulation is loaded.");
            List<Pair<String, Object>> envValues = new ArrayList<>();
            for (Node view : vboxEnvVariables.getChildren()) {
                if (view instanceof InputItemView<?>) {
                    if (view instanceof TextInputItemView) {
                        if (((TextInputItemView) view).getTextField().getText().isEmpty()) {

                        }
                    }
                    Pair<String, Object> pair = new Pair<>(((InputItemView<?>) view).getTitle(), ((InputItemView<?>) view).getValue());
                    envValues.add(pair);
                }
            }
            engineManager.setEnvironmentValues(envValues);

            engineManager.runSimulation();

            mainController.switchToResultsTab();
        }
    }

    private boolean checkForInvalidItems(VBox container) {
        for (Node view : container.getChildren()) {
            if (view instanceof InputItemView<?>) {
                if (!((InputItemView<?>) view).isValid()) {
                    return true; // Found an invalid item
                }
            }
        }
        return false; // No invalid items found
    }

    private void showErrorAlert(String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    private void showConfirmationAlert(String title, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}

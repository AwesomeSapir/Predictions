package ui.subcomponent.execution;

import dto.detail.DTOEntity;
import dto.detail.DTOEnvironmentVariable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import ui.customcomponent.view.EntityPopulationView;
import ui.customcomponent.view.EnvironmentVariableView;
import ui.customcomponent.view.item.InputItemView;

import java.util.Collection;

public class ExecutionController {
    @FXML
    public VBox vboxEntityPopulation;
    @FXML
    public VBox vboxEnvVariables;
    @FXML
    public Button buttonClear;
    @FXML
    public Button buttonStart;

    private ObservableList<DTOEntity> entities;

    private ObservableList<DTOEnvironmentVariable> environmentVariables;

    @FXML
    public void initialize() {
        entities = FXCollections.observableArrayList();
        environmentVariables = FXCollections.observableArrayList();
    }

    public void setEntities(Collection<DTOEntity> entities) {
        this.entities.clear();
        vboxEntityPopulation.getChildren().clear();
        this.entities.addAll(entities);
        for (DTOEntity entity : entities) {
            vboxEntityPopulation.getChildren().addAll(
                    new EntityPopulationView(entity),
                    new Separator(Orientation.HORIZONTAL));
        }
    }

    public void setEnvironmentVariables(Collection<DTOEnvironmentVariable> environmentVariables) {
        this.environmentVariables.clear();
        vboxEnvVariables.getChildren().clear();
        this.environmentVariables.addAll(environmentVariables);
        for (DTOEnvironmentVariable environmentVariable : environmentVariables) {
            vboxEnvVariables.getChildren().addAll(
                    new EnvironmentVariableView(environmentVariable).getView(),
                    new Separator(Orientation.HORIZONTAL));
        }
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
            showConfirmationAlert("Success", "Everything is fine.");
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contentText);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // User clicked OK, you can perform any desired action here
            }
        });
    }
}

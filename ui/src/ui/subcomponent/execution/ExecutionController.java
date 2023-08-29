package ui.subcomponent.execution;

import dto.detail.DTOEntity;
import dto.detail.DTOEnvironmentVariable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import ui.customcomponent.view.EntityPopulationView;
import ui.customcomponent.view.EnvironmentVariableView;

import java.util.Collection;

public class ExecutionController {
    @FXML public VBox vboxEntityPopulation;
    @FXML public VBox vboxEnvVariables;

    private ObservableList<DTOEntity> entities;

    private ObservableList<DTOEnvironmentVariable> environmentVariables;

    @FXML
    public void initialize(){
        entities = FXCollections.observableArrayList();
        environmentVariables = FXCollections.observableArrayList();
    }

    public void setEntities(Collection<DTOEntity> entities) {
        this.entities.clear();
        this.entities.addAll(entities);
        for (DTOEntity entity : entities){
            vboxEntityPopulation.getChildren().addAll(
                    new EntityPopulationView(entity),
                    new Separator(Orientation.HORIZONTAL));
        }
    }

    public void setEnvironmentVariables(Collection<DTOEnvironmentVariable> environmentVariables) {
        this.environmentVariables.clear();
        this.environmentVariables.addAll(environmentVariables);
        for (DTOEnvironmentVariable environmentVariable : environmentVariables){
            vboxEnvVariables.getChildren().addAll(
                    new EnvironmentVariableView(environmentVariable).getView(),
                    new Separator(Orientation.HORIZONTAL));
        }
    }
}

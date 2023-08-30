package ui.subcomponent.execution;

import dto.detail.DTOEntity;
import dto.detail.DTOEnvironmentVariable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import ui.customcomponent.view.EntityPopulationView;
import ui.customcomponent.view.EnvironmentVariableView;
import ui.customcomponent.view.item.InputItemView;

import java.util.Collection;

public class ExecutionController {
    @FXML public VBox vboxEntityPopulation;
    @FXML public VBox vboxEnvVariables;
    @FXML public Button buttonClear;
    @FXML public Button buttonStart;

    private ObservableList<DTOEntity> entities;

    private ObservableList<DTOEnvironmentVariable> environmentVariables;

    @FXML
    public void initialize(){
        entities = FXCollections.observableArrayList();
        environmentVariables = FXCollections.observableArrayList();
        buttonClear.setOnMouseClicked(event -> {
            System.out.println("click");
            for (Node view : vboxEntityPopulation.getChildren()){
                if(view instanceof InputItemView<?>) {
                    System.out.println("inside if");
                    ((InputItemView<?>) view).clear();
                }
            }
            for (Node view : vboxEnvVariables.getChildren()){
                if(view instanceof InputItemView<?>) {
                    ((InputItemView<?>) view).clear();
                }
            }
        });
    }

    public void setEntities(Collection<DTOEntity> entities) {
        this.entities.clear();
        vboxEntityPopulation.getChildren().clear();
        this.entities.addAll(entities);
        for (DTOEntity entity : entities){
            vboxEntityPopulation.getChildren().addAll(
                    new EntityPopulationView(entity),
                    new Separator(Orientation.HORIZONTAL));
        }
    }

    public void setEnvironmentVariables(Collection<DTOEnvironmentVariable> environmentVariables) {
        this.environmentVariables.clear();
        vboxEnvVariables.getChildren().clear();
        this.environmentVariables.addAll(environmentVariables);
        for (DTOEnvironmentVariable environmentVariable : environmentVariables){
            vboxEnvVariables.getChildren().addAll(
                    new EnvironmentVariableView(environmentVariable).getView(),
                    new Separator(Orientation.HORIZONTAL));
        }
    }
}

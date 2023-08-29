package ui.subcomponent.execution;

import dto.detail.DTOEntity;
import dto.detail.DTOEnvironmentVariable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import ui.subcomponent.execution.view.EntityListCell;
import ui.subcomponent.execution.view.EnvironmentVariableListCell;

import java.util.Collection;

public class ExecutionController {

    @FXML public ListView<DTOEntity> listEntityPopulations;
    @FXML public ListView<DTOEnvironmentVariable> listEnvironmentVariables;

    private ObservableList<DTOEntity> entities;

    private ObservableList<DTOEnvironmentVariable> environmentVariables;

    @FXML
    public void initialize(){
        entities = FXCollections.observableArrayList();
        environmentVariables = FXCollections.observableArrayList();

        listEntityPopulations.setCellFactory(param -> new EntityListCell());
        listEntityPopulations.setItems(entities);

        listEnvironmentVariables.setCellFactory(param -> new EnvironmentVariableListCell());
        listEnvironmentVariables.setItems(environmentVariables);

    }

    public void setEntities(Collection<DTOEntity> entities) {
        this.entities.addAll(entities);
    }

    public void setEnvironmentVariables(Collection<DTOEnvironmentVariable> environmentVariables) {
        this.environmentVariables.addAll(environmentVariables);
    }
}
package ui.subcomponent.execution;

import dto.detail.DTOEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import ui.subcomponent.execution.view.EntityListCell;

import java.util.Collection;

public class ExecutionController {

    @FXML public ListView<DTOEntity> listEntityPopulations;

    private ObservableList<DTOEntity> entities;

    @FXML
    public void initialize(){
        entities = FXCollections.observableArrayList();
        listEntityPopulations.setCellFactory(param -> new EntityListCell());
        listEntityPopulations.setItems(entities);
    }

    public void setEntities(Collection<DTOEntity> entities) {
        this.entities.addAll(entities);
    }
}

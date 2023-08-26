package ui.subcomponent.detail;

import dto.detail.*;
import dto.detail.action.DTOAction;
import dto.detail.action.DTOActionCondition;
import dto.simulation.DTOSimulationDetails;
import engine.EngineInterface;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailsController {

    @FXML private TreeView treeViewDetails;
    @FXML private TableView tableViewDetails;

    private EngineInterface engine;
    private SimpleBooleanProperty isFileSelected;

    @FXML
    public void initialize(){
        TableColumn<Map.Entry<String, String>, String> fieldColumn = new TableColumn<>("Field");
        fieldColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));

        TableColumn<Map.Entry<String, String>, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue()));
        tableViewDetails.getColumns().addAll(fieldColumn, valueColumn);
    }

    private void loadTree(){
        DTOSimulationDetails simulationDetails = engine.getSimulationDetails();
        TreeItem<Object> rootItem = new TreeItem<>("Simulation");
        TreeItem<Object> branchEnvVars = new TreeItem<>("Environment Variables");
        TreeItem<Object> branchEntities = new TreeItem<>("Entities");
        TreeItem<Object> branchRules = new TreeItem<>("Rules");
        TreeItem<Object> branchTermination = new TreeItem<>("Termination");

        List<DTOEnvironmentVariable> environmentVariables = new ArrayList<>(engine.getEnvironmentDefinitions());
        for(DTOEnvironmentVariable envVar: environmentVariables){
            TreeItem<Object> itemEnvVar = new TreeItem<>(envVar);
            branchEnvVars.getChildren().add(itemEnvVar);
        }

        for (DTOEntity entity : simulationDetails.getEntities()) {
            TreeItem<Object> itemEntity = new TreeItem<>(entity);
            branchEntities.getChildren().add(itemEntity);
            List<DTOProperty> properties = new ArrayList<>(entity.getProperties());
            for (DTOProperty property : properties) {
                itemEntity.getChildren().add(new TreeItem<>(property));
            }
        }

        for (DTORule rule : simulationDetails.getRules()) {
            TreeItem<Object> itemRule = new TreeItem<>(rule);
            branchRules.getChildren().add(itemRule);
            for (DTOAction action : rule.getActions()) {
                TreeItem<Object> itemAction = new TreeItem<>(action);
                itemRule.getChildren().add(itemAction);
                if(action instanceof DTOActionCondition){
                    TreeItem<Object> itemThen = new TreeItem<>("Then");
                    for (DTOAction actionThen : ((DTOActionCondition) action).getActionsThen()){
                        itemThen.getChildren().add(new TreeItem<>(actionThen));
                    }
                    TreeItem<Object> itemElse = new TreeItem<>("Else");
                    for (DTOAction actionElse : ((DTOActionCondition) action).getActionsElse()){
                        itemElse.getChildren().add(new TreeItem<>(actionElse));
                    }
                    itemAction.getChildren().addAll(itemThen, itemElse);
                }
            }
        }

        DTOTermination termination = simulationDetails.getTermination();

        if (termination.getTicks() != null) {
            TreeItem<Object> itemTermination = new TreeItem<>("Ticks");
            branchTermination.getChildren().add(itemTermination);
        }
        if (termination.getSeconds() != null) {
            TreeItem<Object> itemTermination = new TreeItem<>("Seconds");
            branchTermination.getChildren().add(itemTermination);
        }

        rootItem.getChildren().addAll(branchEnvVars, branchEntities, branchRules, branchTermination);
        treeViewDetails.setRoot(rootItem);
    }

    public void selectItem() {
        TreeItem<Object> item = (TreeItem<Object>) treeViewDetails.getSelectionModel().getSelectedItem();
        tableViewDetails.getItems().clear();
        if(item != null && item.getValue() instanceof DTOObject){
            DTOObject dtoObject = (DTOObject) item.getValue();
            ObservableList<Map.Entry<String, String>> tableData = FXCollections.observableArrayList(dtoObject.getFieldValueMap().entrySet());
            tableViewDetails.setItems(tableData);
        }
    }

    public void setEngine(EngineInterface engine) {
        this.engine = engine;
    }

    public void setIsFileSelected(SimpleBooleanProperty isFileSelected) {
        this.isFileSelected = isFileSelected;
        this.isFileSelected.addListener((observable, oldValue, newValue) -> loadTree());
    }
}
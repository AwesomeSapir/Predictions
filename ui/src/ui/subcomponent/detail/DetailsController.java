package ui.subcomponent.detail;

import dto.detail.*;
import dto.simulation.DTOSimulationDetails;
import engine.EngineInterface;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.ArrayList;
import java.util.List;

public class DetailsController {

    private EngineInterface engine;
    private SimpleBooleanProperty isFileSelected;

    @FXML private TreeView treeViewDetails;

    private void loadTree(){
        DTOSimulationDetails simulationDetails = engine.getSimulationDetails();
        TreeItem<String> rootItem = new TreeItem<>("Simulation");
        TreeItem<String> branchEnvVars = new TreeItem<>("Environment Variables");
        TreeItem<String> branchEntities = new TreeItem<>("Entities");
        TreeItem<String> branchRules = new TreeItem<>("Rules");
        TreeItem<String> branchTermination = new TreeItem<>("Termination");


        List<DTOEnvironmentVariable> environmentVariables = new ArrayList<>(engine.getEnvironmentDefinitions());
        for(DTOEnvironmentVariable envVar: environmentVariables){
            TreeItem<String> itemEnvVar = new TreeItem<>(envVar.getName());
            branchEnvVars.getChildren().add(itemEnvVar);
        }

        for (DTOEntity entity : simulationDetails.getEntities()) {
            TreeItem<String> itemEntity = new TreeItem<>(entity.getName());
            branchEntities.getChildren().add(itemEntity);
            List<DTOProperty> properties = new ArrayList<>(entity.getProperties());
            for (DTOProperty property : properties) {
                itemEntity.getChildren().add(new TreeItem<>(property.getName()));
            }
        }

        for (DTORule rule : simulationDetails.getRules()) {
            TreeItem<String> itemRule = new TreeItem<>(rule.getName());
            branchRules.getChildren().add(itemRule);
            for (String action : rule.getActions()) {
                itemRule.getChildren().add(new TreeItem<>(action));
            }
        }

        DTOTermination termination = simulationDetails.getTermination();

        if (termination.getTicks() != null) {
            TreeItem<String> itemTermination = new TreeItem<>("Ticks");
            TreeItem<String> ticksAmount = new TreeItem<>(termination.getTicks().toString());
            itemTermination.getChildren().add(ticksAmount);
            branchTermination.getChildren().add(itemTermination);
        }
        if (termination.getSeconds() != null) {
            TreeItem<String> itemTermination = new TreeItem<>("Seconds");
            TreeItem<String> secondsAmount = new TreeItem<>(termination.getSeconds().toString());
            itemTermination.getChildren().add(secondsAmount);
            branchTermination.getChildren().add(itemTermination);
        }

        rootItem.getChildren().addAll(branchEnvVars, branchEntities, branchRules, branchTermination);
        treeViewDetails.setRoot(rootItem);
    }

    public void selectItem() {

        TreeItem<String> item = (TreeItem<String>) treeViewDetails.getSelectionModel().getSelectedItem();

        if (item != null) {
            System.out.println(item.getValue());
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
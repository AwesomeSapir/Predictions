package ui.subcomponent.detail;

import dto.detail.DTOEntity;
import dto.detail.DTOProperty;
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

        for (DTOEntity entity : simulationDetails.getEntities()) {
            TreeItem<String> itemEntity = new TreeItem<>(entity.getName());
            branchEntities.getChildren().add(itemEntity);
            List<DTOProperty> properties = new ArrayList<>(entity.getProperties());
            for (DTOProperty property : properties) {
                itemEntity.getChildren().add(new TreeItem<>(property.getName()));
            }
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
package ui.component.subcomponent.detail;

import dto.detail.*;
import dto.detail.action.DTOAction;
import dto.detail.action.DTOActionCondition;
import dto.simulation.DTOSimulationDetails;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import ui.component.custom.detail.DetailView;
import ui.engine.EngineManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailsController {
    @FXML public VBox paneRight;
    @FXML private TreeView<Object> treeViewDetails;

    private EngineManager engineManager;

    private DetailView viewDetailController;

    @FXML
    public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Objects.requireNonNull(getClass().getResource("/ui/component/custom/detail/viewDetail.fxml")));
            paneRight.getChildren().add(loader.load());
            viewDetailController = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTree() {
        DTOSimulationDetails simulationDetails = engineManager.getSimulationDetails();
        TreeItem<Object> rootItem = new TreeItem<>("Simulation");
        TreeItem<Object> branchEnvVars = new TreeItem<>("Environment Variables");
        TreeItem<Object> branchEntities = new TreeItem<>("Entities");
        TreeItem<Object> branchRules = new TreeItem<>("Rules");
        TreeItem<Object> branchTermination = new TreeItem<>("Termination");

        List<DTOEnvironmentVariable> environmentVariables = new ArrayList<>(engineManager.getEnvironmentDefinitions());
        for (DTOEnvironmentVariable envVar : environmentVariables) {
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
                if (action instanceof DTOActionCondition) {
                    TreeItem<Object> itemThen = new TreeItem<>("Then");
                    for (DTOAction actionThen : ((DTOActionCondition) action).getActionsThen()) {
                        itemThen.getChildren().add(new TreeItem<>(actionThen));
                    }
                    TreeItem<Object> itemElse = new TreeItem<>("Else");
                    for (DTOAction actionElse : ((DTOActionCondition) action).getActionsElse()) {
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
        if (item != null && item.getValue() instanceof DTOObject) {
            DTOObject dtoObject = (DTOObject) item.getValue();
            displayObject(dtoObject);
        }
    }

    private void displayObject(DTOObject object){
        if(object instanceof DTOEntity){
            viewDetailController.setEntity((DTOEntity) object);
        } else if (object instanceof DTOProperty){
            viewDetailController.setProperty((DTOProperty) object);
        } else if (object instanceof DTORule){
            viewDetailController.setRule((DTORule) object);
        } else if (object instanceof DTOEnvironmentVariable){
            viewDetailController.setEnvVariable((DTOEnvironmentVariable) object);
        }
    }

    public void setEngineManager(EngineManager engineManager) {
        this.engineManager = engineManager;
        engineManager.isSimulationLoadedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                loadTree();
            }
        });
    }
}
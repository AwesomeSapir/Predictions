package ui.component.subcomponent.detail;

import dto.detail.*;
import dto.detail.action.DTOAction;
import dto.detail.action.DTOActionCondition;
import dto.simulation.DTOSimulationDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import ui.component.custom.detail.entity.EntityDetailView;
import ui.component.custom.detail.property.PropertyDetailView;
import ui.engine.EngineManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DetailsController {
    @FXML public VBox paneRight;
    @FXML private TreeView<Object> treeViewDetails;

    private EngineManager engineManager;

    private Node viewEntityDetails;
    private EntityDetailView entityViewController;
    private Node viewPropertyDetails;
    private PropertyDetailView propertyViewController;
    private Node viewRuleDetails;

    @FXML
    public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Objects.requireNonNull(getClass().getResource("/ui/component/custom/detail/entity/viewDetailEntity.fxml")));
            viewEntityDetails = loader.load();
            entityViewController = loader.getController();

            loader = new FXMLLoader();
            loader.setLocation(Objects.requireNonNull(getClass().getResource("/ui/component/custom/detail/property/viewDetailProperty.fxml")));
            viewPropertyDetails = loader.load();
            propertyViewController = loader.getController();


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
        //tableViewDetails.getItems().clear();
        if (item != null && item.getValue() instanceof DTOObject) {
            DTOObject dtoObject = (DTOObject) item.getValue();
            displayObject(dtoObject);
            ObservableList<Map.Entry<String, String>> tableData = FXCollections.observableArrayList(dtoObject.getFieldValueMap().entrySet());
            //tableViewDetails.setItems(tableData);
        }
    }

    private void displayObject(DTOObject object){
        if(object instanceof DTOEntity){
            entityViewController.setObject((DTOEntity) object);
            setDetailView(viewEntityDetails);
        } else if (object instanceof DTOProperty){
            propertyViewController.setObject((DTOProperty) object);
            setDetailView(viewPropertyDetails);
        } else {

            setDetailView(viewRuleDetails);
        }
    }

    private void setDetailView(Node view){
        if(!paneRight.getChildren().isEmpty() && !paneRight.getChildren().get(0).equals(view)){
            paneRight.getChildren().clear();
            paneRight.getChildren().add(view);
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
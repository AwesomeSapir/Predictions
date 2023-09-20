package ui.component.subcomponent.detail;

import dto.detail.*;
import dto.detail.action.DTOAction;
import dto.detail.action.DTOActionCondition;
import dto.simulation.DTOSimulationDetails;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import ui.component.custom.detail.DetailView;
import ui.engine.EngineManager;
import ui.style.Animations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailsController {
    @FXML public VBox paneRight;
    @FXML private TreeView<Object> treeViewDetails;

    private EngineManager engineManager;

    private DetailView viewDetailController;
    private Node viewDetail;

    @FXML
    public void initialize() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Objects.requireNonNull(getClass().getResource("/ui/component/custom/detail/viewDetail.fxml")));
            paneRight.getChildren().add(loader.load());
            viewDetailController = loader.getController();
            viewDetail = loader.getRoot();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        treeViewDetails.setCellFactory(new Callback<TreeView<Object>, TreeCell<Object>>() {
            @Override
            public TreeCell<Object> call(TreeView<Object> param) {
                return new TreeCell<Object>(){
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            if(item instanceof DTOObject) {
                                setText(((DTOObject) item).getName());
                            } else {
                                setText(item.toString());
                            }
                        }
                    }

                    @Override
                    public void updateSelected(boolean selected) {
                        super.updateSelected(selected);
                        if(selected){
                            Animations.bounceRight(this);
                        }
                    }
                };
            }
        });

        treeViewDetails.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.getValue() instanceof DTOObject) {
                DTOObject dtoObject = (DTOObject) newValue.getValue();
                displayObject(dtoObject);
            }
        });

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
        TreeItem<Object> itemTermination = new TreeItem<>(termination);

        DTOGrid grid = simulationDetails.getGrid();
        TreeItem<Object> itemGrid = new TreeItem<>(grid);

        rootItem.getChildren().addAll(itemGrid, branchEnvVars, branchEntities, branchRules, itemTermination);
        treeViewDetails.setRoot(rootItem);
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
        } else if (object instanceof DTOTermination) {
            viewDetailController.setTermination((DTOTermination) object);
        } else if(object instanceof DTOGrid){
            viewDetailController.setGrid((DTOGrid) object);
        }
        Animations.expandingCircle(viewDetail);
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
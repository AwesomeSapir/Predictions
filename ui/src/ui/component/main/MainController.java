package ui.component.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import ui.component.subcomponent.detail.DetailsController;
import ui.component.subcomponent.execution.ExecutionController;
import ui.component.subcomponent.result.ResultsController;
import ui.engine.EngineManager;
import ui.engine.Simulation;

import java.io.File;

public class MainController {

    public TabPane mainTabPane;
    private EngineManager engineManager;

    @FXML private BorderPane mainBorderPane;
    @FXML private Button chooseFileButton;
    @FXML private TextField filePathTextField;
    @FXML private DetailsController tabDetailsController;
    @FXML private ExecutionController tabExecutionController;

    @FXML private ResultsController tabResultsController;

    @FXML private Tab tabResultsID;
    @FXML public Tab tabDetailsID;
    @FXML public Tab tabExecutionID;

    @FXML
    void chooseXMLFile(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        File selectedFile = fileChooser.showOpenDialog(chooseFileButton.getScene().getWindow());
        if(selectedFile == null){
            return;
        }

        engineManager.loadSimulation(selectedFile);
    }

    public void setEngineManager(EngineManager engineManager) {
        this.engineManager = engineManager;
        tabDetailsController.setEngineManager(engineManager);
        tabExecutionController.setEngineManager(engineManager);
        tabResultsController.setEngineManager(engineManager);

        filePathTextField.textProperty().bind(engineManager.simulationPathProperty());

        //TODO deltetetetetete before submitting
        File selectedFile = new File("C:\\Users\\melch\\Downloads\\master-ex1.xml");
        if(selectedFile.exists()){
            System.out.println("sapir exists");
            engineManager.loadSimulation(selectedFile);
        } else {
            selectedFile = new File("C:\\Users\\micha\\Downloads\\master-ex1.xml");
            if (selectedFile.exists()) {
                System.out.println("tal exists");
                engineManager.loadSimulation(selectedFile);
            }
        }
    }

    @FXML
    public void initialize() {
        tabExecutionController.setMainController(this);
        mainTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals(tabExecutionID)){
                tabExecutionController.onFocus();
            }
            if(oldValue.equals(tabExecutionID)){
                tabExecutionController.onUnfocused();
            }
        });
    }

    public void switchToResultsTab() {
        mainTabPane.getSelectionModel().select(tabResultsID); // Switch to tabResultController
        ListView<Simulation> listView = tabResultsController.listExecution;
        listView.selectionModelProperty().get().select(listView.getItems().size()-1);
    }

}

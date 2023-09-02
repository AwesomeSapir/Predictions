package ui.main;

import engine.EngineInterface;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import ui.subcomponent.detail.DetailsController;
import ui.subcomponent.execution.ExecutionController;
import ui.subcomponent.result.ResultsController;

import java.io.File;

public class MainController {

    private EngineInterface engine;
    private SimpleBooleanProperty isFileSelected = new SimpleBooleanProperty(false);
    private SimpleStringProperty filePath = new SimpleStringProperty();

    @FXML private BorderPane mainBorderPane;
    @FXML private Button chooseFileButton;
    @FXML private TextField filePathTextField;
    @FXML private DetailsController tabDetailsController;
    @FXML private ExecutionController tabExecutionController;

    @FXML private ResultsController tabResultController;

    @FXML
    void chooseXMLFile(ActionEvent event){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        File selectedFile = fileChooser.showOpenDialog(chooseFileButton.getScene().getWindow());
        //filePathTextField.setText(selectedFile.getAbsolutePath());
        if(selectedFile == null){
            return;
        }
        engine.loadXml(selectedFile.getAbsolutePath());
        isFileSelected.set(true);
        filePath.set(selectedFile.getAbsolutePath());

        tabExecutionController.setEntities(engine.getSimulationDetails().getEntities());
        tabExecutionController.setEnvironmentVariables(engine.getEnvironmentDefinitions());
    }

    public void setEngine(EngineInterface engine) {
        this.engine = engine;
        tabDetailsController.setEngine(engine);

        //TODO deltetetetetete before submitting
        engine.loadXml("C:\\Users\\melch\\Downloads\\master-ex1.xml");
        isFileSelected.set(true);
        filePath.set("C:\\Users\\melch\\Downloads\\master-ex1.xml");

        tabExecutionController.setEntities(engine.getSimulationDetails().getEntities());
        tabExecutionController.setEnvironmentVariables(engine.getEnvironmentDefinitions());

    }

    @FXML
    public void initialize() {
        tabDetailsController.setIsFileSelected(isFileSelected);
        filePathTextField.textProperty().bind(filePath);
    }
}

package ui.component.main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ui.component.subcomponent.detail.DetailsController;
import ui.component.subcomponent.execution.ExecutionController;
import ui.component.subcomponent.result.ResultsController;
import ui.engine.EngineManager;
import ui.engine.Simulation;
import ui.style.Animations;
import ui.style.StyleManager;

import java.io.File;
import java.io.IOException;

public class MainController {

    @FXML
    public TabPane mainTabPane;
    @FXML
    public Button buttonSettings;
    private EngineManager engineManager;

    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private Button chooseFileButton;
    @FXML
    private TextField filePathTextField;
    @FXML
    private DetailsController tabDetailsController;
    @FXML
    private ExecutionController tabExecutionController;

    @FXML
    private ResultsController tabResultsController;

    @FXML
    private Tab tabResultsID;
    @FXML
    public Tab tabDetailsID;
    @FXML
    public Tab tabExecutionID;

    private final Stage settingsStage = new Stage();

    @FXML
    void chooseXMLFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        File selectedFile = fileChooser.showOpenDialog(chooseFileButton.getScene().getWindow());
        if (selectedFile == null) {
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
        if (selectedFile.exists()) {
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
            if (newValue.equals(tabExecutionID)) {
                tabExecutionController.onFocus();
            }
            if (oldValue.equals(tabExecutionID)) {
                tabExecutionController.onUnfocused();
            }
        });

        initSettingsWindow();
        buttonSettings.setOnMouseEntered(event -> Animations.spin(buttonSettings, 0, 90).play());
        buttonSettings.setOnMouseExited(event -> Animations.spin(buttonSettings, 90, -90).play());
        buttonSettings.setOnAction(event -> showSettingsWindow());
    }

    public void initSettingsWindow(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/component/subcomponent/settings/settingsScreen.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            StyleManager.register(scene);
            settingsStage.setTitle("Settings");
            settingsStage.setScene(scene);
            settingsStage.setOnCloseRequest(value -> StyleManager.unregister(scene));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showSettingsWindow(){
        if(!settingsStage.isShowing()){
            settingsStage.show();
        } else {
            settingsStage.setIconified(false);
            settingsStage.toFront();
        }
    }

    public void switchToResultsTab() {
        mainTabPane.getSelectionModel().select(tabResultsID); // Switch to tabResultController
        ListView<Simulation> listView = tabResultsController.listExecution;
        listView.selectionModelProperty().get().select(listView.getItems().size() - 1);
    }

}

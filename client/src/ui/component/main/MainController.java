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

    @FXML public TabPane mainTabPane;
    @FXML public Button buttonSettings;
    private EngineManager engineManager;

    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private Button buttonFileChoose;
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

        File selectedFile = fileChooser.showOpenDialog(buttonFileChoose.getScene().getWindow());
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
    }

    @FXML
    public void initialize() {
        tabExecutionController.setMainController(this);
        tabResultsController.setMainController(this);
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
            settingsStage.setTitle("Settings");
            settingsStage.setScene(scene);
            settingsStage.setOnCloseRequest(value -> StyleManager.getInstance().unregister(scene));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showSettingsWindow(){
        StyleManager.getInstance().register(settingsStage.getScene());
        if(!settingsStage.isShowing()){
            settingsStage.show();
            settingsStage.setMinHeight(settingsStage.getScene().getHeight());
            settingsStage.setMinWidth(settingsStage.getScene().getWidth());
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

    public void switchToExecutionTab(int simulationId){
        mainTabPane.getSelectionModel().select(tabExecutionID);
        tabExecutionController.restoreValues(simulationId);
    }

}

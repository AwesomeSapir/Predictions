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
import ui.component.subcomponent.queue.QueueController;
import ui.component.subcomponent.result.ResultsController;
import ui.engine.EngineManager;
import ui.engine.Simulation;
import ui.style.Animations;
import ui.style.StyleManager;

import java.io.File;
import java.io.IOException;

public class MainController {

    @FXML public TabPane mainTabPane;
    @FXML public Button buttonSettings, buttonQueue;
    private EngineManager engineManager;

    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private Button buttonFileChoose;
    @FXML
    private TextField textFieldFilePath;
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

    private final Stage queueStage = new Stage();

    public void initQueueWindow(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/component/subcomponent/queue/screenQueue.fxml"));
            Parent root = loader.load();
            QueueController queueController = loader.getController();
            queueController.setQueue(engineManager.getQueue());
            Scene scene = new Scene(root);
            queueStage.setTitle("Queue");
            queueStage.setScene(scene);
            queueStage.setOnCloseRequest(value -> StyleManager.unregister(scene));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showQueueWindow(){
        StyleManager.register(queueStage.getScene());
        if(!queueStage.isShowing()){
            queueStage.show();
        } else {
            queueStage.setIconified(false);
            queueStage.toFront();
        }
    }

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

        textFieldFilePath.textProperty().bind(engineManager.simulationPathProperty());

        //TODO deltetetetetete before submitting
        File selectedFile = new File("C:\\Users\\melch\\Downloads\\ex2-virus.xml");
        if(selectedFile.exists()){
            System.out.println("sapir exists");
            engineManager.loadSimulation(selectedFile);
        } else {
            selectedFile = new File("C:\\Users\\micha\\Downloads\\ex2-virus.xml");
            if (selectedFile.exists()) {
                System.out.println("tal exists");
                engineManager.loadSimulation(selectedFile);
            }
        }
        initQueueWindow();
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
        buttonQueue.setOnAction(event -> showQueueWindow());
    }

    public void initSettingsWindow(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/component/subcomponent/settings/settingsScreen.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            settingsStage.setTitle("Settings");
            settingsStage.setScene(scene);
            settingsStage.setOnCloseRequest(value -> StyleManager.unregister(scene));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showSettingsWindow(){
        StyleManager.register(settingsStage.getScene());
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

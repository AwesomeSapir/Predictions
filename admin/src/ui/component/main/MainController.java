package ui.component.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import ui.component.subcomponent.management.ManagementController;
import ui.component.subcomponent.queue.QueueController;
import ui.component.subcomponent.result.ResultsController;
import ui.engine.EngineManager;
import ui.engine.Simulation;
import ui.style.Animations;
import ui.style.StyleManager;

import java.io.IOException;

public class MainController {

    @FXML public TabPane mainTabPane;
    @FXML public Button buttonSettings, buttonQueue;
    private EngineManager engineManager;

    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ManagementController tabDetailsController;
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
            queueStage.setOnCloseRequest(value -> StyleManager.getInstance().unregister(scene));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void showQueueWindow(){
        StyleManager.getInstance().register(queueStage.getScene());
        if(!queueStage.isShowing()){
            queueStage.show();
        } else {
            queueStage.setIconified(false);
            queueStage.toFront();
        }
    }

    public void setEngineManager(EngineManager engineManager) {
        this.engineManager = engineManager;
        tabDetailsController.setEngineManager(engineManager);
        tabResultsController.setEngineManager(engineManager);

        initQueueWindow();
    }

    @FXML
    public void initialize() {
        tabResultsController.setMainController(this);

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

}

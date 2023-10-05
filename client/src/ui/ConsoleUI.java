package ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.controlsfx.control.NotificationPane;
import ui.component.main.MainController;
import ui.engine.EngineManager;
import ui.style.StyleManager;

public class ConsoleUI extends Application implements MainUI{

    private EngineManager engineManager = new EngineManager();

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Predictions");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/ui/component/main/mainScreen.fxml"));
        Parent root = loader.load();

        MainController mainController = loader.getController();
        mainController.setEngineManager(engineManager);

        NotificationPane notificationPane = Notify.getInstance().register(root);
        AnchorPane.setTopAnchor(notificationPane, 0.0);
        AnchorPane.setBottomAnchor(notificationPane, 0.0);
        AnchorPane.setLeftAnchor(notificationPane, 0.0);
        AnchorPane.setRightAnchor(notificationPane, 0.0);
        AnchorPane parent = new AnchorPane(notificationPane);

        Scene scene = new Scene(parent, 1000, 800);
        StyleManager.getInstance().register(scene);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(400);
        primaryStage.setOnCloseRequest(event -> {
            engineManager.shutdown();
            Platform.exit();
            System.exit(0);
        });
    }

    @Override
    public void run(String[] args) {
        launch(args);
    }
}

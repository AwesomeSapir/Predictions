package ui;

import javafx.animation.PauseTransition;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.util.Duration;
import org.controlsfx.control.NotificationPane;
import ui.style.StyleManager;

public class Notify {

    private static Notify instance;
    private NotificationPane notificationPane;
    private final PauseTransition hideDelay = new PauseTransition(Duration.seconds(3));

    private Notify() {
    }

    public static NotificationPane init(Node root) {
        if (instance == null) {
            instance = new Notify();
        }

        if (instance.notificationPane == null) {
            instance.notificationPane = new NotificationPane(root);
            instance.notificationPane.setShowFromTop(false);
        }
        return instance.notificationPane;
    }

    public static Notify getInstance() {
        return instance;
    }

    public void showAlertDialog(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        StyleManager.register(alert.getDialogPane().getScene());
        alert.setOnCloseRequest(event -> StyleManager.unregister(alert.getDialogPane().getScene()));
        alert.showAndWait();
    }

    public void showAlertBar(String text) {
        notificationPane.show(text);
        notificationPane.setOnShowing(event -> {
            hideDelay.setOnFinished(e -> notificationPane.hide());
            hideDelay.play();
        });
        notificationPane.setOnHidden(event -> {
            hideDelay.stop();
        });
    }

}

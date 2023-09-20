package ui;

import javafx.animation.PauseTransition;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;
import org.controlsfx.control.NotificationPane;
import ui.style.StyleManager;

import java.util.Optional;

public class Notify {

    private static Notify instance;
    private NotificationPane notificationPane = new NotificationPane();
    private final PauseTransition hideDelay = new PauseTransition(Duration.seconds(3));

    private Notify() {
        notificationPane.setShowFromTop(false);
    }

    public NotificationPane register(Node root) {
        notificationPane.setContent(root);
        return notificationPane;
    }

    public static Notify getInstance() {
        if (instance == null) {
            instance = new Notify();
        }
        return instance;
    }

    public Optional<ButtonType> showAlertDialog(String title, String header, String content, Alert.AlertType type) {
        Alert alert;
        if(type == Alert.AlertType.CONFIRMATION){
            alert = new Alert(type, content, ButtonType.YES, ButtonType.NO);
        } else {
            alert = new Alert(type);
        }
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        StyleManager.getInstance().register(alert.getDialogPane().getScene());
        alert.setOnCloseRequest(event -> StyleManager.getInstance().unregister(alert.getDialogPane().getScene()));
        return alert.showAndWait();
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

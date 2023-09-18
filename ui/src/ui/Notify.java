package ui;

import javafx.animation.PauseTransition;
import javafx.scene.Node;
import javafx.util.Duration;
import org.controlsfx.control.NotificationPane;

public class Notify {

    private static Notify instance;
    private NotificationPane notificationPane;
    private final PauseTransition hideDelay = new PauseTransition(Duration.seconds(3));

    private Notify(){
    }

    public static NotificationPane init(Node root){
        if(instance == null){
            instance = new Notify();
        }

        if(instance.notificationPane == null){
            instance.notificationPane = new NotificationPane(root);
            instance.notificationPane.setShowFromTop(false);
        }
        return instance.notificationPane;
    }

    public static Notify getInstance(){
        return instance;
    }

    public void showAlertBar(String text){
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

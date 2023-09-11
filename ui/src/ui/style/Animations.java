package ui.style;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Animations {

    public static void shake(Node node){
        Timeline shakeTimeline = new Timeline();

        KeyValue kvStart = new KeyValue(node.translateXProperty(), 0);

        // Starting looser with wider translation values
        KeyFrame kf1 = new KeyFrame(Duration.millis(50), new KeyValue(node.translateXProperty(), -15));
        KeyFrame kf2 = new KeyFrame(Duration.millis(100), new KeyValue(node.translateXProperty(), 15));

        // Getting tighter
        KeyFrame kf3 = new KeyFrame(Duration.millis(150), new KeyValue(node.translateXProperty(), -10));
        KeyFrame kf4 = new KeyFrame(Duration.millis(200), new KeyValue(node.translateXProperty(), 10));

        // Tightest shake
        KeyFrame kf5 = new KeyFrame(Duration.millis(250), new KeyValue(node.translateXProperty(), -5));
        KeyFrame kf6 = new KeyFrame(Duration.millis(300), new KeyValue(node.translateXProperty(), 5));

        KeyFrame kfEnd = new KeyFrame(Duration.millis(350), kvStart);

        shakeTimeline.getKeyFrames().addAll(kf1, kf2, kf3, kf4, kf5, kf6, kfEnd);
        shakeTimeline.playFromStart();
    }

    public static void expandingCircle(Node node) {
        // Create the circle that will start from the center of the node
        Circle circle = new Circle(0);
        circle.setCenterX(node.getLayoutBounds().getWidth() / 2);
        circle.setCenterY(node.getLayoutBounds().getHeight() / 2);

        // Use the circle as a mask for the target node
        node.setClip(circle);

        // Prepare the animation: expand the circle from radius 0 to half of the diagonal of the node
        double endRadius = Math.sqrt(Math.pow(node.getLayoutBounds().getWidth(), 2) + Math.pow(node.getLayoutBounds().getHeight(), 2)) / 2;
        Timeline timeline = new Timeline();

        KeyValue kv = new KeyValue(circle.radiusProperty(), endRadius);
        KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
        timeline.getKeyFrames().add(kf);

        timeline.setOnFinished(event -> node.setClip(null));
        // Start the animation
        timeline.play();
    }

    public static void highlight(Node node, int repeat){
        ScaleTransition pulse = new ScaleTransition(Duration.millis(200), node);
        pulse.setFromX(1);
        pulse.setFromY(1);
        pulse.setToX(1.2);
        pulse.setToY(1.2);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(repeat * 2);
        pulse.play();
    }

}

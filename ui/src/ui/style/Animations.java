package ui.style;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Animations {

    public static final int LONG = 1000;
    public static final int MEDIUM = 200;
    public static final int SHORT = 100;
    private static final int DEFAULT_BOUNCE_BY = 16;

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

        KeyFrame kfEnd = new KeyFrame(Duration.millis(MEDIUM), kvStart);

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
        KeyFrame kf = new KeyFrame(Duration.millis(MEDIUM), kv);
        timeline.getKeyFrames().add(kf);

        timeline.setOnFinished(event -> node.setClip(null));
        // Start the animation
        timeline.play();
    }

    public static void highlight(Node node, int repeat){
        ScaleTransition pulse = new ScaleTransition(Duration.millis(MEDIUM), node);
        pulse.setFromX(1);
        pulse.setFromY(1);
        pulse.setToX(1.2);
        pulse.setToY(1.2);
        pulse.setAutoReverse(true);
        pulse.setCycleCount(repeat * 2);
        pulse.play();
    }

    public static Transition spin(Node node, int from, int degrees){
        RotateTransition rotate = new RotateTransition(Duration.millis(MEDIUM), node);
        rotate.setByAngle(degrees);
        rotate.setAutoReverse(false);
        rotate.setCycleCount(1); // 1 forward, 1 back
        rotate.setOnFinished(event -> node.setRotate(from + degrees));
        return rotate;
    }

    public static void bounceY(Node node, int by){
        TranslateTransition nudge = new TranslateTransition(Duration.millis(SHORT), node);
        nudge.setByY(by);
        nudge.setAutoReverse(true);
        nudge.setCycleCount(2);
        nudge.play();
    }

    public static void bounceX(Node node, int by){
        TranslateTransition nudge = new TranslateTransition(Duration.millis(SHORT), node);
        nudge.setByX(by);
        nudge.setAutoReverse(true);
        nudge.setCycleCount(2);
        nudge.play();
    }

    public static void bounceUp(Node node){
        bounceY(node, -DEFAULT_BOUNCE_BY);
    }

    public static void bounceDown(Node node){
        bounceY(node, DEFAULT_BOUNCE_BY);
    }

    public static void bounceRight(Node node){
        bounceX(node, DEFAULT_BOUNCE_BY);
    }

    public static void bounceLeft(Node node){
        bounceX(node, -DEFAULT_BOUNCE_BY);
    }

    public static void fadeTransition(Scene scene, Runnable actionToPerform) {
        BackgroundFill fill = ((Pane)scene.getRoot()).getBackground().getFills().get(0);
        Rectangle blackRectangle = new Rectangle(scene.getWidth(), scene.getHeight(), fill.getFill());
        Pane root = (Pane) scene.getRoot();
        root.getChildren().add(blackRectangle);

        // Create fade-in transition for the black rectangle
        FadeTransition fadeIn = new FadeTransition(Duration.millis(MEDIUM), blackRectangle);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setOnFinished(e -> {
            // Perform the style change (or any other actions)
            actionToPerform.run();

            // Create fade-out transition for the black rectangle
            FadeTransition fadeOut = new FadeTransition(Duration.millis(MEDIUM), blackRectangle);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(ev -> {
                // Remove the black rectangle after the transition
                root.getChildren().remove(blackRectangle);
            });
            fadeOut.play();
        });
        fadeIn.play();
    }

    public static ParallelTransition createShrinkAndSpin(Node node) {
        Duration duration = Duration.millis(SHORT);
        // Create a scale transition
        ScaleTransition scaleTransition = new ScaleTransition(duration, node);
        scaleTransition.setToX(0.0);
        scaleTransition.setToY(0.0);

        // Create a rotation transition
        RotateTransition rotateTransition = new RotateTransition(duration, node);
        rotateTransition.setByAngle(180);
        rotateTransition.setOnFinished(event -> node.setRotate(180));

        // Run both transitions simultaneously
        ParallelTransition parallelTransition = new ParallelTransition(
                //scaleTransition,
                rotateTransition
        );
        return parallelTransition;
    }

    public static ParallelTransition createGrowAndSpin(Node node) {
        Duration duration = Duration.millis(SHORT);
        // Create a scale transition
        ScaleTransition scaleTransition = new ScaleTransition(duration, node);
        scaleTransition.setToX(1.0); // Assuming original size is 1
        scaleTransition.setToY(1.0); // Assuming original size is 1

        // Create a rotation transition
        RotateTransition rotateTransition = new RotateTransition(duration, node);
        rotateTransition.setByAngle(180);
        rotateTransition.setOnFinished(event -> node.setRotate(0));

        // Run both transitions simultaneously
        ParallelTransition parallelTransition = new ParallelTransition(
                //scaleTransition,
                rotateTransition
        );
        return parallelTransition;
    }
}

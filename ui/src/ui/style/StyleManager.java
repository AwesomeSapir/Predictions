package ui.style;

import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StyleManager {

    private static final List<Scene> scenes = new ArrayList<>();
    private static final String main = "main.css";
    private static final String PATH = "/ui/resources/style/";
    private static Mode currentMode = Mode.DARK;
    private static Color currentColor = Color.BLUE;

    public static void register(Scene scene){
        scenes.add(scene);
        updateStyles(scene);
    }

    public static void unregister(Scene scene) {
        scenes.remove(scene);
    }

    private static void updateStyles(Scene scene) {
        scene.getStylesheets().clear();
        scene.getStylesheets().addAll(
                Objects.requireNonNull(StyleManager.class.getResource(PATH + main)).toExternalForm(),
                Objects.requireNonNull(StyleManager.class.getResource(PATH + "/mode/" + currentMode.toString().toLowerCase() + ".css")).toExternalForm(),
                Objects.requireNonNull(StyleManager.class.getResource(PATH + "/color/" + currentColor.toString().toLowerCase() + ".css")).toExternalForm()
        );
    }

    public static void changeMode(Mode newMode) {
        currentMode = newMode;
        updateAll();
    }

    public static void changeColor(Color newColor) {
        currentColor = newColor;
        updateAll();
    }

    private static void updateAll() {
        for (Scene scene : scenes) {
            updateStyles(scene);
        }
    }

}

package ui.style;

import javafx.scene.Scene;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class StyleManager {

    private final List<Scene> scenes = new ArrayList<>();
    private final String main = "main.css";
    private final String PATH = "/ui/resources/style/";
    private Mode currentMode = Mode.DARK;
    private Color currentColor = Color.BLUE;
    private Font currentFont = Font.Poppins;
    private FontSize currentFontSize = FontSize.medium;

    public void register(Scene scene) {
        scenes.add(scene);
        scene.getStylesheets().clear();
        scene.getStylesheets().add(Objects.requireNonNull(StyleManager.class.getResource(PATH + main)).toExternalForm());
        updateStyles(scene);
    }

    public void unregister(Scene scene) {
        scenes.remove(scene);
    }

    private void updateStyles(Scene scene) {
        updateMode(scene);
        updateColor(scene);
        updateFont(scene);
        updateFontSize(scene);
    }

    private String modeString;
    private String colorString;
    private String fontString;
    private String fontSizeString;

    private void updateMode(Scene scene) {
        scene.getStylesheets().remove(modeString);
        modeString = Objects.requireNonNull(StyleManager.class.getResource(PATH + "mode/" + currentMode.toString().toLowerCase() + ".css")).toExternalForm();
        scene.getStylesheets().add(modeString);
    }

    private void updateColor(Scene scene) {
        scene.getStylesheets().remove(colorString);
        colorString = Objects.requireNonNull(StyleManager.class.getResource(PATH + "color/" + currentColor.toString().toLowerCase() + ".css")).toExternalForm();
        scene.getStylesheets().add(colorString);
    }

    private void updateFont(Scene scene) {
        scene.getStylesheets().remove(fontString);
        fontString = Objects.requireNonNull(StyleManager.class.getResource(PATH + "font/" + currentFont.toString().toLowerCase() + ".css")).toExternalForm();
        scene.getStylesheets().add(fontString);
    }

    private void updateFontSize(Scene scene){
        scene.getStylesheets().remove(fontSizeString);
        fontSizeString = Objects.requireNonNull(StyleManager.class.getResource(PATH + "size/" + currentFontSize.toString().toLowerCase() + ".css")).toExternalForm();
        scene.getStylesheets().add(fontSizeString);
    }

    public void changeMode(Mode newMode) {
        currentMode = newMode;
        updateAll(this::updateMode);
    }

    public void changeColor(Color newColor) {
        currentColor = newColor;
        updateAll(this::updateColor);
    }

    public void changeFont(Font newFont) {
        currentFont = newFont;
        updateAll(this::updateFont);
    }

    public void changeFontSize(FontSize newFontSize){
        currentFontSize = newFontSize;
        updateAll(this::updateFontSize);
    }

    private void updateAll(SceneUpdater action) {
        for (Scene scene : scenes) {
            action.update(scene);
            //Animations.fadeTransition(scene, () -> );
        }
    }

    private void initFonts() {
        List<String> fonts = Arrays.asList(
                "AmaticSC-Bold.ttf",
                "AmaticSC-Regular.ttf",
                "ChakraPetch-Bold.ttf",
                "ChakraPetch-Regular.ttf",
                "DancingScript-Bold.ttf",
                "DancingScript-Regular.ttf",
                "PermanentMarker-Regular.ttf",
                "PlayfairDisplay-Bold.ttf",
                "PlayfairDisplay-Regular.ttf",
                "Poppins-Bold.ttf",
                "Poppins-Regular.ttf",
                "Roboto-Bold.ttf",
                "Roboto-Regular.ttf",
                "RobotoSlab-Bold.ttf",
                "RobotoSlab-Regular.ttf"
        );
        for (String font : fonts){
            URL url = StyleManager.class.getResource("/ui/resources/font/" + font);
            if(url!=null){
                javafx.scene.text.Font.loadFont(url.toExternalForm(), 24);
            }
        }
    }

    public Mode getCurrentMode() {
        return currentMode;
    }

    public Color getCurrentColor() {
        return currentColor;
    }

    public Font getCurrentFont() {
        return currentFont;
    }

    public FontSize getCurrentFontSize() {
        return currentFontSize;
    }

    @FunctionalInterface
    private interface SceneUpdater {
        void update(Scene scene);
    }

    private static StyleManager instance;
    private StyleManager() {
        initFonts();
    }

    public static StyleManager getInstance(){
        if(instance == null){
            instance = new StyleManager();
        }
        return instance;
    }

}

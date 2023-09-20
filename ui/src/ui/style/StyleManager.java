package ui.style;

import javafx.scene.Scene;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
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
        Path path = null;
        try {
            path = Paths.get(Objects.requireNonNull(StyleManager.class.getResource("/ui/resources/font/")).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try {
            Files.walkFileTree(path, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(".ttf") || file.toString().endsWith(".otf")) {
                        try {
                            javafx.scene.text.Font.loadFont(Files.newInputStream(file), 12); // 12 is the default font size, you can give your own
                            System.out.println("font loaded: " + file.getFileName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        /*
        Font[] fonts = Font.values();
        for (Font font : fonts){
            URL url = StyleManager.class.getResource("/ui/resources/font/" + font + "-Regular.ttf");
            if(url!=null){
                javafx.scene.text.Font.loadFont(url.toExternalForm(), 24);
            }
            url = StyleManager.class.getResource("/ui/resources/font/" + font + "-Bold.ttf");
            if(url!=null){
                javafx.scene.text.Font.loadFont(url.toExternalForm(), 24);
            }
        }*/
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

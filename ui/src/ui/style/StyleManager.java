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

    private static final List<Scene> scenes = new ArrayList<>();
    private static final String main = "main.css";
    private static final String PATH = "/ui/resources/style/";
    private static Mode currentMode = Mode.DARK;
    private static Color currentColor = Color.BLUE;
    private static Font currentFont = Font.Poppins;

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
                Objects.requireNonNull(StyleManager.class.getResource(PATH + "mode/" + currentMode.toString().toLowerCase() + ".css")).toExternalForm(),
                Objects.requireNonNull(StyleManager.class.getResource(PATH + "color/" + currentColor.toString().toLowerCase() + ".css")).toExternalForm(),
                Objects.requireNonNull(StyleManager.class.getResource(PATH + "font/" + currentFont.toString().toLowerCase() + ".css")).toExternalForm()
        );
    }

    private static void updateMode(Scene scene){
        scene.getStylesheets().remove(1);
        scene.getStylesheets().add(1, Objects.requireNonNull(StyleManager.class.getResource(PATH + "mode/" + currentMode.toString().toLowerCase() + ".css")).toExternalForm());
    }

    private static void updateColor(Scene scene){
        scene.getStylesheets().remove(2);
        scene.getStylesheets().add(2, Objects.requireNonNull(StyleManager.class.getResource(PATH + "color/" + currentColor.toString().toLowerCase() + ".css")).toExternalForm());
    }

    private static void updateFont(Scene scene){
        scene.getStylesheets().remove(3);
        scene.getStylesheets().add(3, Objects.requireNonNull(StyleManager.class.getResource(PATH + "font/" + currentFont.toString().toLowerCase() + ".css")).toExternalForm());
    }

    public static void changeMode(Mode newMode) {
        currentMode = newMode;
        updateAll(StyleManager::updateMode);
    }

    public static void changeColor(Color newColor) {
        currentColor = newColor;
        updateAll(StyleManager::updateColor);
    }

    public static void changeFont(Font newFont){
        currentFont = newFont;
        updateAll(StyleManager::updateFont);
    }

    private static void updateAll(SceneUpdater action){
        for (Scene scene : scenes) {
            Animations.fadeTransition(scene, () -> action.update(scene));
        }
    }

    private static void updateAll() {
        for (Scene scene : scenes) {
            Animations.fadeTransition(scene, () -> updateStyles(scene));
            //updateStyles(scene);
        }
    }

    private static void initFonts(){
        Path path = null;
        try {
            path = Paths.get(Objects.requireNonNull(StyleManager.class.getResource("/ui/resources/font/")).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        try {
            Files.walkFileTree(path, EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new SimpleFileVisitor<Path>(){
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

    public static void init(){
        initFonts();
    }

    public static Mode getCurrentMode() {
        return currentMode;
    }

    public static Color getCurrentColor() {
        return currentColor;
    }

    public static Font getCurrentFont() {
        return currentFont;
    }

    @FunctionalInterface
    private interface SceneUpdater {
        void update(Scene scene);
    }
}

package splash.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

public class ResourceManager {
    private static ResourceBundle bundle;
    private static final ObjectProperty<Locale> currentLocale = new SimpleObjectProperty<>(Locale.ENGLISH);

    static {
        loadLanguage(Config.DEFAULT_LANGUAGE);
    }

    public static void loadLanguage(String lang) {
        Locale locale = Locale.forLanguageTag(lang);
        try {
            bundle = ResourceBundle.getBundle("splash/lang/messages", locale);
            currentLocale.set(locale);
        } catch (MissingResourceException e) {
            System.err.println("Language file not found: " + lang);
            bundle = ResourceBundle.getBundle("splash/lang/messages", Locale.ENGLISH);
            currentLocale.set(Locale.ENGLISH);
        }
    }

    public static ObjectProperty<Locale> currentLocaleProperty() {
        return currentLocale;
    }

    public static String getString(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return "!" + key + "!";
        }
    }

    public static Image getFishImage(int fishNumber, boolean isLeft) {
        if (fishNumber < 1 || fishNumber > Config.FISH_IMAGE_COUNT) {
            System.err.println("Fish number must be between 1 and " + Config.FISH_IMAGE_COUNT);
            return null;
        }
        String path = String.format("/images/characters/fish-%d-%s.png", fishNumber, isLeft ? "left" : "right");
        return loadImage(path);
    }

    public static Image getWaterTexture(){
        return loadImage("/images/environment/texture-water.png");
    }

    public static Image getMountainImage(int mountainNumber) {
        String path = String.format("/images/environment/mountain-%d.png", mountainNumber);
        return loadImage(path);
    }

    public static Image getSeaWeedsImage(int seaweedNumber) {
        String path = String.format("/images/environment/seaweed-%d.png", seaweedNumber);
        return loadImage(path);
    }

    public static Image getRockImage(int rockNumber) {
        String path = String.format("/images/environment/rock-%d.png", rockNumber);
        return loadImage(path);
    }

    private static Image loadImage(String path) {
        try (InputStream is = ResourceManager.class.getResourceAsStream(path)) {
            return new Image(is);
        } catch (IOException | NullPointerException e) {
            System.err.println("Error loading " + " image: " + path);
            return null;
        }
    }

    public static String getStyleSheet() {
        return ResourceManager.class.getResource("/css/styles.css").toExternalForm();
    }
}
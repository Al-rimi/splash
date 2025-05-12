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
        loadLanguage("en");
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

    public static Image getEnemyImage(int fishNumber, boolean isLeft) {
        String path = String.format("/images/characters/enemy/fish-%d-%s.png", fishNumber, isLeft ? "left" : "right");
        return loadImage(path, "enemy");
    }

    public static Image getFoodImage(int fishNumber, boolean isLeft) {
        String path = String.format("/images/characters/food/fish-%d-%s.png", fishNumber, isLeft ? "left" : "right");
        return loadImage(path, "food");
    }

    public static Image getPlayerImage(String character, boolean isLeft) {
        String path = String.format("/images/characters/players/%s-%s.png", character, isLeft ? "left" : "right");
        return loadImage(path, "player");
    }

    private static Image loadImage(String path, String type) {
        try (InputStream is = ResourceManager.class.getResourceAsStream(path)) {
            return new Image(is);
        } catch (IOException | NullPointerException e) {
            System.err.println("Error loading " + type + " image: " + path);
            return null;
        }
    }

    public static String getStyleSheet() {
        return ResourceManager.class.getResource("/css/styles.css").toExternalForm();
    }
}
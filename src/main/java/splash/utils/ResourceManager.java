package splash.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

public class ResourceManager {
    private static ResourceBundle bundle;
    private static ObjectProperty<Locale> currentLocale = new SimpleObjectProperty<>(Locale.ENGLISH);

    static {
        loadLanguage("en");
    }

    public static void loadLanguage(String lang) {
        Locale locale = Locale.of(lang);
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
        try (InputStream is = ResourceManager.class.getResourceAsStream(path)) {
            return new Image(is);
        } catch (IOException e) {
            System.err.println("Error loading enemy image: " + path);
            return null;
        }
    }

    public static Image getFoodImage(int fishNumber, boolean isLeft) {
        String path = String.format("/images/characters/food/fish-%d-%s.png", fishNumber, isLeft ? "left" : "right");
        try (InputStream is = ResourceManager.class.getResourceAsStream(path)) {
            return new Image(is);
        } catch (IOException e) {
            System.err.println("Error loading food image: " + path);
            return null;
        }
    }

    public static Image getPlayerImage(String character, boolean isLeft) {
        String path = String.format("/images/characters/players/%s-%s.png", character, isLeft ? "left" : "right");
        try (InputStream is = ResourceManager.class.getResourceAsStream(path)) {
            return new Image(is);
        } catch (IOException e) {
            System.err.println("Error loading player image: " + path);
            return null;
        }
    }
}
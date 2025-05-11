package splash.managers;

import java.util.*;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ResourceManager {
    private static ResourceBundle bundle;
    private static ObjectProperty<Locale> currentLocale = 
        new SimpleObjectProperty<>(Locale.ENGLISH);

    static { loadLanguage("en"); }

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
}
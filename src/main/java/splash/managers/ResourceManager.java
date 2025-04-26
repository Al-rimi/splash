package splash.managers;

import java.util.*;

public class ResourceManager {
    private static ResourceBundle bundle;
    
    static {
        // Load default language on initialization
        loadLanguage("en");
    }
    
    public static void loadLanguage(String lang) {
        try {
            bundle = ResourceBundle.getBundle("splash/lang/messages", 
                    Locale.of(lang));
        } catch (MissingResourceException e) {
            System.err.println("Language file not found: " + lang);
            bundle = ResourceBundle.getBundle("splash/lang/messages", 
                    Locale.ENGLISH);
        }
    }
    
    public static String getString(String key) {
        try {
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return "!" + key + "!";
        }
    }
}
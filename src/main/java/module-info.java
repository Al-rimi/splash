module splash {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.media;
    requires java.desktop;
    
    // opens splash.lang to java.base;     // Removed the opens directive for splash.lang as the package does not exist or is empty
    exports splash.application;
    exports splash.core.entities;
    exports splash.managers;
}
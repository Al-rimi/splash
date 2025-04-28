module splash {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.media;
    requires java.desktop;
    
    exports splash.application;
    exports splash.core.entities;
    exports splash.managers;
}
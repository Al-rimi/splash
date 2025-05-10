module splash {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.media;
    requires java.desktop;
    
    exports splash.application;
    exports splash.entities;
    exports splash.managers;
}
module splash {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.media;
    
    exports splash.application;
    exports splash.core.entities;
    exports splash.managers;
}
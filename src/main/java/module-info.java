module splash {
    requires javafx.controls;
    requires transitive javafx.graphics;
    requires javafx.media;
    requires java.desktop;
	requires javafx.base;
    
    exports splash.application;
    exports splash.entities;
    exports splash.utils;
    exports splash.systems;
}
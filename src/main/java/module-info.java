module com.syalux.splash {
    requires javafx.controls;
    requires transitive javafx.media;
    requires javafx.graphics;
    requires javafx.base;
    
    opens com.syalux.splash.application to javafx.graphics;
    exports com.syalux.splash.application;
}
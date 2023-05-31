module uk.co.conoregan.showrenamer {
    requires com.github.benmanes.caffeine;
    requires info.movito.themoviedbapi;
    requires java.prefs;
    requires javafx.fxml;
    requires javafx.controls;
    requires org.apache.commons.io;
    requires org.slf4j;

    opens uk.co.conoregan.showrenamer to javafx.fxml, javafx.graphics;
    opens uk.co.conoregan.showrenamer.controller to javafx.fxml;

    exports uk.co.conoregan.showrenamer;
    exports uk.co.conoregan.showrenamer.api;
    exports uk.co.conoregan.showrenamer.config.preference;
    exports uk.co.conoregan.showrenamer.config.property;
    exports uk.co.conoregan.showrenamer.controller;
    exports uk.co.conoregan.showrenamer.suggestion;
    exports uk.co.conoregan.showrenamer.util;
}
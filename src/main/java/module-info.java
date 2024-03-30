module uk.co.conoregan.showrenamer {
    requires info.movito.themoviedbapi;
    requires java.prefs;
    requires javafx.fxml;
    requires javafx.controls;
    requires org.apache.commons.io;
    requires org.slf4j;

    opens uk.co.conoregan.showrenamer to javafx.fxml, javafx.graphics;
    opens uk.co.conoregan.showrenamer.controller to javafx.fxml;
}
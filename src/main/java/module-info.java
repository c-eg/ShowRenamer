module ShowRenamer {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires themoviedbapi;

    opens uk.co.conoregan.showrenamer to javafx.graphics;
    opens uk.co.conoregan.showrenamer.controller to javafx.fxml;
    opens images;
    opens properties;
    opens style;
    opens view;
    exports uk.co.conoregan.showrenamer;
}
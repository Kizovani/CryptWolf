module com.cryptwolf.cryptwolf {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.bootstrapicons;
    requires org.kordamp.ikonli.coreui;
/*
TODO: Testing dependencies, cant get these working at all:
requires org.junit.jupiter.api;
    requires org.testfx.junit5;
    requires org.testfx.core;
 */


    opens com.cryptwolf.cryptwolf to javafx.fxml;
    exports com.cryptwolf.cryptwolf;
}
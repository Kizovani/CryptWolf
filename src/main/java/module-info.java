module com.cryptwolf.cryptwolf {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.bootstrapicons;
    requires org.kordamp.ikonli.coreui;

    opens com.cryptwolf.cryptwolf to javafx.fxml;
    exports com.cryptwolf.cryptwolf;
}
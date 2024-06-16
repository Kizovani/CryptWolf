module com.cryptwolf.cryptwolf {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;


    opens com.cryptwolf.cryptwolf to javafx.fxml;
    exports com.cryptwolf.cryptwolf;
}
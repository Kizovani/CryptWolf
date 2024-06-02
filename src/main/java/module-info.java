module com.cryptwolf.cryptwolf {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.cryptwolf.cryptwolf to javafx.fxml;
    exports com.cryptwolf.cryptwolf;
}
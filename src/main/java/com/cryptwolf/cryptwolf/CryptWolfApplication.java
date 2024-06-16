package com.cryptwolf.cryptwolf;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.util.Objects;

/*COLORS:
Funky Green: #00b69b
Cool Red/Pink: #f53368
Dark Blue: #0a0021
Dark Purple: #2a0143
Skin Color LMFAO: #fefbd8
 */

/*ICONS (In ikonli)
folder: bi-folder
double arrows: cil-transfer
 */

import java.io.IOException;
import java.util.Objects;

public class CryptWolfApplication extends Application {

    private static PrimaryController controller;


    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(CryptWolfApplication.class.getResource("primary.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Font.loadFont(getClass().getResourceAsStream("/fonts/YAY USA Regular.ttf"), 10);
        controller = fxmlLoader.getController();
        primaryStage.setTitle("CryptWolf");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Icons/CryptWolf_Icon_Transparent_CROPPED.png"))));
        primaryStage.initStyle(StageStyle.UNDECORATED); //Removing default windows title bar
        primaryStage.setScene(scene);
        primaryStage.show();


        // Debugging: Check if the font file is accessible
        /*
        if (getClass().getResourceAsStream("/Fonts/yayusa.ttf") == null) {
            System.err.println("Font resource not found");
        } else {
            System.out.println("Font resource found");
        }
         */

        // Debugging: Check if the CSS file is accessible
        /*
        if (getClass().getResource("/Styles/styles.css") == null) {
            System.err.println("CSS resource not found");
        } else {
            System.out.println("CSS resource found");
        }
         */

        // Load and apply the CSS file
        scene.getStylesheets().add(getClass().getResource("/Styles/styles.css").toExternalForm());

    }

    public static PrimaryController getController() {
        return controller;
    }


    public static void main(String[] args) {
        launch();
    }
}

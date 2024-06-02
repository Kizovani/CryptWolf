package com.cryptwolf.cryptwolf;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/*COLORS:
Funky Green: #00b69b
Cool Red/Pink: #f53368
Dark Blue: #0a0021
Skin Color LMFAO: #fefbd8
 */

import java.io.IOException;

public class CryptWolfApplication extends Application {
    double x = 0, y = 0;
    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(CryptWolfApplication.class.getResource("primary.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        primaryStage.setTitle("CryptWolf");
        //to remove the default windows title bar
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}

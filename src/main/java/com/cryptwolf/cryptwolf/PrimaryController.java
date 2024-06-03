package com.cryptwolf.cryptwolf;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class PrimaryController {

    private File sourceDirectory;
    private File destinationDirectory;
    private SecretKey secretKey;
    private boolean isEncryptMode = true;
    private double xOffset = 0;
    private double yOffset = 0;

    public PrimaryController() {
        // Generate a new AES key
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256); // AES-256
            this.secretKey = keyGen.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private ToggleButton toggleButton;

    @FXML
    private Button actionButton;

    @FXML
    private AnchorPane draggableRegion;

    @FXML
    private void handleCloseButtonAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleSelectSourceFolder(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Source Folder");
        sourceDirectory = directoryChooser.showDialog(((Node) event.getSource()).getScene().getWindow());

        if (sourceDirectory != null) {
            // Handle the selected directory
            showAlert("Source Folder Selected", sourceDirectory.getAbsolutePath());
        } else {
            showAlert("No Source Folder Selected", "Please select a source folder.");
        }
    }

    @FXML
    private void handleSelectDestinationFolder(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Destination Folder");
        destinationDirectory = directoryChooser.showDialog(((Node) event.getSource()).getScene().getWindow());

        if (destinationDirectory != null) {
            // Handle the selected directory
            showAlert("Destination Folder Selected", destinationDirectory.getAbsolutePath());
        } else {
            showAlert("No Destination Folder Selected", "Please select a destination folder.");
        }
    }

    @FXML
    private void handleEncryptAndMoveFiles(ActionEvent event) {
        if (sourceDirectory != null && destinationDirectory != null) {
            try {
                if (isEncryptMode) {
                    processFiles(sourceDirectory.toPath(), destinationDirectory.toPath(), Cipher.ENCRYPT_MODE);
                    showAlert("Success", "Files encrypted and moved successfully.");
                } else {
                    processFiles(sourceDirectory.toPath(), destinationDirectory.toPath(), Cipher.DECRYPT_MODE);
                    showAlert("Success", "Files decrypted and moved successfully.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "An error occurred: " + e.getMessage());
            }
        } else {
            showAlert("Error", "Please select both source and destination folders.");
        }
    }

    @FXML
    private void toggleMode(ActionEvent event) {
        isEncryptMode = !isEncryptMode;
        toggleButton.setText(isEncryptMode ? "Encrypt" : "Decrypt");
        actionButton.setText(isEncryptMode ? "Encrypt" : "Decrypt");
    }

    private void processFiles(Path sourcePath, Path destinationPath, int cipherMode) throws Exception {
        Files.walk(sourcePath)
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    try {
                        Path destFile = destinationPath.resolve(sourcePath.relativize(file));
                        Files.createDirectories(destFile.getParent());
                        processFile(file, destFile, cipherMode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void processFile(Path sourceFile, Path destFile, int cipherMode) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(cipherMode, secretKey);

        byte[] fileBytes = Files.readAllBytes(sourceFile);
        byte[] processedBytes = cipher.doFinal(fileBytes);

        Files.write(destFile, processedBytes);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //initialize app with events for dragging
    @FXML
    public void initialize() {
        draggableRegion.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        draggableRegion.setOnMouseDragged(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }
}
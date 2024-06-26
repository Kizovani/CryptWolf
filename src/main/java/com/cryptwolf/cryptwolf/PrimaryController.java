package com.cryptwolf.cryptwolf;
//TODO: NEXT FEATURE IS TO IMPLEMENT PASSPHRASE

import com.cryptwolf.cryptwolf.exceptions.KeyFileSaveCancelledException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.List;

public class PrimaryController {

    File sourceDirectory;
    File destinationDirectory;
    File keyFileDirectory;
    private SecretKey secretKey;
    boolean isEncryptMode = true;
    private double xOffset = 0;
    private double yOffset = 0;
    private byte[] keyBytes; // To store the generated key bytes for transport and reading

    @FXML
    private ToggleButton toggleButton;

    @FXML
    private Button actionButton;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private VBox EncryptPageOptions;

    @FXML
    private VBox DecryptPageOptions;

    @FXML
    private ChoiceBox<String> keyLengthChoiceBox;

    @FXML
    private CheckBox deleteOriginalCheckBox;

    @FXML
    private AnchorPane draggableRegion;

    @FXML
    private Button sourceDirectoryButton;

    @FXML
    private Label sourceDirectoryLabel;

    @FXML
    private Button destinationDirectoryButton;

    @FXML
    private Label destinationDirectoryLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private CheckBox keyFileCheckBoxEncrypt;

    @FXML
    private CheckBox keyFileCheckBoxDecrypt;

    @FXML
    private CheckBox encryptWithPhraseCheckBox;

    @FXML
    private CheckBox decryptWithPhraseCheckBox;

    @FXML
    private Button existingKeyFileEncryptButton;

    @FXML
    private Button keyFileDecryptButton;

    @FXML
    private PasswordField passwordFieldEncrypt;

    @FXML
    private PasswordField passwordFieldDecrypt;


    @FXML
    private void handleCloseButtonAction(ActionEvent event) {
        clearSensitiveData();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleSelectFolder(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(((Node) event.getSource()).getScene().getWindow());

        if (event.getSource() == sourceDirectoryButton) {
            if (selectedDirectory != null) {
                sourceDirectory = selectedDirectory;
                sourceDirectoryLabel.setText(sourceDirectory.getAbsolutePath());
            } else {
                sourceDirectoryLabel.setText("No Source Folder Selected");
            }
        } else if (event.getSource() == destinationDirectoryButton) {
            if (selectedDirectory != null) {
                destinationDirectory = selectedDirectory;
                destinationDirectoryLabel.setText(destinationDirectory.getAbsolutePath());
            } else {
                destinationDirectoryLabel.setText("No Destination Folder Selected");
            }
        }
    }

    @FXML
    private void handleSwapFolders(ActionEvent event) {
        File temp = sourceDirectory;
        sourceDirectory = destinationDirectory;
        destinationDirectory = temp;

        sourceDirectoryLabel.setText(sourceDirectory != null ?  sourceDirectory.getAbsolutePath() : "No Source Folder Selected");
        destinationDirectoryLabel.setText(destinationDirectory != null ? destinationDirectory.getAbsolutePath() : "No Destination Folder Selected");
    }

    @FXML
    private void handleEncryptAndMoveFiles(ActionEvent event) {
        if (sourceDirectory != null && destinationDirectory != null) {
            try {
                // Check if the source directory is empty
                boolean isEmpty = Files.list(sourceDirectory.toPath()).findAny().isEmpty();
                if (isEmpty) {
                    showAlert("Error", "The source directory is empty. Please select a directory with files.");
                    return;
                }
            } catch (IOException e) {
                showAlert("Error", "An error occurred while checking the source directory: " + e.getMessage());
                return;
            }

            String keyLengthStr = keyLengthChoiceBox.getValue();
            int keyLength = 256;
            if (keyLengthStr.equals("128 bits")) {
                keyLength = 128;
            } else if (keyLengthStr.equals("192 bits")) {
                keyLength = 192;
            }

            try {
                if (isEncryptMode) {
                    String password = passwordFieldEncrypt.getText();
                    if (!password.isEmpty()) {
                        secretKey = generateKeyFromPassword(password, keyLength);
                    } else if (keyFileCheckBoxEncrypt.isSelected() && keyFileDirectory != null) {
                        keyBytes = loadKeyFromFile(keyFileDirectory);
                        secretKey = new SecretKeySpec(keyBytes, "AES");
                    } else {
                        secretKey = generateKey(keyLength);
                        keyBytes = secretKey.getEncoded();
                        if (keyFileCheckBoxEncrypt.isSelected()) {
                            try {
                                saveKeyToFile(keyBytes);
                                if (!keyFileDirectory.exists()) {
                                    showAlert("Error", "No directory selected for the key file. Encryption aborted.");
                                    return;
                                }
                            } catch (KeyFileSaveCancelledException e) {
                                e.printStackTrace();
                                showAlert("Error", "An error occurred while saving the key to a file: " + e.getMessage());
                                return;
                            }
                        }
                    }
                } else {
                    String password = passwordFieldDecrypt.getText();
                    if (!password.isEmpty()) {
                        secretKey = generateKeyFromPassword(password, keyLength);
                    } else if (keyFileCheckBoxDecrypt.isSelected()) {
                        if (!keyFileDirectory.exists()) {
                            showAlert("Error", "Key file not found or null for decryption");
                            return;
                        }
                        keyBytes = loadKeyFromFile(keyFileDirectory);
                        secretKey = new SecretKeySpec(keyBytes, "AES");
                    } else {
                        if (keyBytes == null) {
                            showAlert("Error", "No key found for decryption.");
                            return;
                        }
                        secretKey = new SecretKeySpec(keyBytes, "AES");
                    }
                }

                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            List<Path> files = Files.walk(sourceDirectory.toPath())
                                    .filter(Files::isRegularFile)
                                    .toList();

                            int totalFiles = files.size();
                            for (int i = 0; i < totalFiles; i++) {
                                Path file = files.get(i);
                                Path destFile = destinationDirectory.toPath().resolve(sourceDirectory.toPath().relativize(file));
                                Files.createDirectories(destFile.getParent());
                                processFile(file, destFile, isEncryptMode ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE);
                                updateProgress(i + 1, totalFiles);

                                // Delete the original file if the checkbox is selected
                                if (deleteOriginalCheckBox.isSelected()) {
                                    Files.delete(file);
                                }
                            }

                            // Delete the source directory if the checkbox is selected
                            if (deleteOriginalCheckBox.isSelected()) {
                                Files.walk(sourceDirectory.toPath())
                                        .sorted((path1, path2) -> path2.compareTo(path1)) // Sort in reverse order
                                        .forEach(path -> {
                                            try {
                                                Files.delete(path);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        });
                            }

                            Platform.runLater(() -> showAlert("Success", "Files processed successfully."));
                        } catch (Exception e) {
                            e.printStackTrace();
                            Platform.runLater(() -> showAlert("Error", "An error occurred: " + e.getMessage()));
                        }
                        return null;
                    }
                };

                progressBar.progressProperty().bind(task.progressProperty());
                new Thread(task).start();
            } catch (Exception e) {
                showAlert("Error", "Failed to generate key: " + e.getMessage());
            }
        } else {
            showAlert("Error", "Please select both source and destination folders.");
        }
    }


    @FXML
    private void toggleMode(ActionEvent event) {
        isEncryptMode = !isEncryptMode;
        actionButton.setText(isEncryptMode ? "Encrypt" : "Decrypt");

        EncryptPageOptions.setVisible(isEncryptMode);
        EncryptPageOptions.setManaged(isEncryptMode);
        DecryptPageOptions.setVisible(!isEncryptMode);
        DecryptPageOptions.setManaged(!isEncryptMode);

        keyFileCheckBoxDecrypt.setSelected(false);
        keyFileCheckBoxEncrypt.setSelected(false);
        encryptWithPhraseCheckBox.setSelected(false);
        decryptWithPhraseCheckBox.setSelected(false);
    }

    private SecretKey generateKey(int keyLength) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(keyLength, new SecureRandom());
        return keyGen.generateKey();
    }

    private SecretKey generateKeyFromPassword(String password, int keyLength) throws Exception {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), new byte[16], 65536, keyLength);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = factory.generateSecret(spec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }


    private void saveKeyToFile(byte[] keyBytes) throws IOException, KeyFileSaveCancelledException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Key File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Key Files", "*.key"));
        File keyFile = fileChooser.showSaveDialog(null); // Prompt to select directory and name for the new file

        if (keyFile != null) {
            try (FileOutputStream fos = new FileOutputStream(keyFile)) {
                fos.write(keyBytes); // Write the key bytes to the new file
                // Save the directory of the key file
                keyFileDirectory = keyFile.getParentFile();
            } catch (IOException e) {
                showAlert("Error", "Failed to save key file.");
                throw e;
            }
        } else {
            throw new KeyFileSaveCancelledException("Key file not saved. User cancelled the save operation.");
        }
    }

    private byte[] loadKeyFromFile(File keyFile) throws IOException {
        byte[] keyBytes;
        try (FileInputStream fis = new FileInputStream(keyFile)) {
            keyBytes = new byte[(int) keyFile.length()];
            fis.read(keyBytes);
        }
        return keyBytes;
    }

    @FXML
    private void handleSelectExistingKeyFile(ActionEvent event) {
        if (keyFileCheckBoxEncrypt.isSelected() || keyFileCheckBoxDecrypt.isSelected()) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Existing Key File");
            File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
            if (selectedFile != null) {
                keyFileDirectory = selectedFile;
                showAlert("Key File Selected", "Key file selected: " + selectedFile.getAbsolutePath());
            } else {
                showAlert("No Key File Selected", "No key file selected.");
            }
        } else {
            showAlert("Error", "Please enable the 'Key File' checkbox before selecting a key file.");
        }
    }



    private void processFile(Path sourceFile, Path destFile, int cipherMode) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(cipherMode, secretKey);

        try (FileInputStream fis = new FileInputStream(sourceFile.toFile());
             FileOutputStream fos = new FileOutputStream(destFile.toFile());
             CipherInputStream cis = (cipherMode == Cipher.DECRYPT_MODE) ? new CipherInputStream(fis, cipher) : null;
             CipherOutputStream cos = (cipherMode == Cipher.ENCRYPT_MODE) ? new CipherOutputStream(fos, cipher) : null) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            if (cipherMode == Cipher.ENCRYPT_MODE) {
                while ((bytesRead = fis.read(buffer)) != -1) {
                    cos.write(buffer, 0, bytesRead);
                }
            } else {
                while ((bytesRead = cis.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
        }
    }

    public void clearSensitiveData() {
        if (secretKey != null) {
            byte[] keyBytes = secretKey.getEncoded();
            for (int i = 0; i < keyBytes.length; i++) {
                keyBytes[i] = 0;
            }
            secretKey = null;
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        Font customFont = Font.loadFont(getClass().getResource("/Fonts/yayusa.ttf").toExternalForm(), 40);
        titleLabel.setFont(customFont);

        EncryptPageOptions.setVisible(true);
        EncryptPageOptions.setManaged(true);
        DecryptPageOptions.setVisible(false);
        DecryptPageOptions.setManaged(false);

        existingKeyFileEncryptButton.setOnAction(this::handleSelectExistingKeyFile);

        keyLengthChoiceBox.setItems(FXCollections.observableArrayList(
                "128 bits",
                "192 bits",
                "256 bits"
        ));
        keyLengthChoiceBox.setValue("256 bits");

        draggableRegion.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        draggableRegion.setOnMouseDragged(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        keyFileCheckBoxEncrypt.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                keyFileCheckBoxDecrypt.setSelected(false);
                encryptWithPhraseCheckBox.setSelected(false);
                passwordFieldEncrypt.setDisable(true);
            }
            existingKeyFileEncryptButton.setDisable(!newValue);
        });

        keyFileCheckBoxDecrypt.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                keyFileCheckBoxEncrypt.setSelected(false);
                decryptWithPhraseCheckBox.setSelected(false);
                passwordFieldDecrypt.setDisable(true);
            }
            keyFileDecryptButton.setDisable(!newValue);
        });

        encryptWithPhraseCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                keyFileCheckBoxEncrypt.setSelected(false);
                keyFileCheckBoxDecrypt.setSelected(false);
                passwordFieldEncrypt.setDisable(false);
            } else {
                passwordFieldEncrypt.setDisable(true);
            }
        });

        decryptWithPhraseCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                keyFileCheckBoxEncrypt.setSelected(false);
                keyFileCheckBoxDecrypt.setSelected(false);
                passwordFieldDecrypt.setDisable(false);
            } else {
                passwordFieldDecrypt.setDisable(true);
            }
        });

        existingKeyFileEncryptButton.setDisable(!keyFileCheckBoxEncrypt.isSelected());
        keyFileDecryptButton.setDisable(!keyFileCheckBoxDecrypt.isSelected());
        passwordFieldEncrypt.setDisable(!encryptWithPhraseCheckBox.isSelected());
        passwordFieldDecrypt.setDisable(!decryptWithPhraseCheckBox.isSelected());
    }
}

//TODO: BRO I CANT TAKE THIS SHIT ANYMORE THIS IS IMPOSSIBLE, SOMETHINGS WRONG WITH DEPENDANCIES, GETTING THIS ERROR:
//:6:29
//java: package org.junit.jupiter.api does not exist

/*
package com.cryptwolf.cryptwolf;

import com.cryptwolf.cryptwolf.PrimaryController;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import javax.crypto.Cipher;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrimaryControllerTest extends ApplicationTest {

    private PrimaryController controller;
    private FxRobot robot;

    @BeforeEach
    public void setUp() throws Exception {
        FxToolkit.registerPrimaryStage();
        FxToolkit.setupApplication(CryptWolfApplication.class);
        robot = new FxRobot();
    }

    @Override
    public void start(Stage stage) throws Exception {
        CryptWolfApplication testApp = (CryptWolfApplication) FxToolkit.setupApplication(CryptWolfApplication.class);
        controller = CryptWolfApplication.getController();
        stage.show();
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.cleanupStages();
    }

    @Test
    public void testKeyEncryptionAndDecryption() {
        testKeyLengthEncryptionAndDecryption("128 bits");
        testKeyLengthEncryptionAndDecryption("192 bits");
        testKeyLengthEncryptionAndDecryption("256 bits");
    }

    private void testKeyLengthEncryptionAndDecryption(String keyLength) {
        // Set the key length
        robot.clickOn("#keyLengthChoiceBox");
        robot.type(KeyCode.DOWN).type(KeyCode.ENTER);

        // Select source and destination folders
        File sourceDir = new File("path/to/source");
        File destDir = new File("path/to/destination");
        controller.sourceDirectory = sourceDir;
        controller.destinationDirectory = destDir;

        // Encrypt the files
        robot.clickOn("#toggleButton");
        robot.clickOn("#actionButton");

        // Check if the files are encrypted
        assertTrue(areFilesProcessedCorrectly(sourceDir, destDir, Cipher.ENCRYPT_MODE));

        // Decrypt the files
        controller.isEncryptMode = false;
        robot.clickOn("#actionButton");

        // Check if the files are decrypted
        assertTrue(areFilesProcessedCorrectly(destDir, sourceDir, Cipher.DECRYPT_MODE));
    }

    @Test
    public void testSwapDirectories() {
        File sourceDir = new File("path/to/source");
        File destDir = new File("path/to/destination");
        controller.sourceDirectory = sourceDir;
        controller.destinationDirectory = destDir;

        robot.clickOn("#swapButton");

        assertEquals(destDir, controller.sourceDirectory);
        assertEquals(sourceDir, controller.destinationDirectory);
    }

    private boolean areFilesProcessedCorrectly(File sourceDir, File destDir, int mode) {
        try {
            List<Path> sourceFiles = Files.walk(sourceDir.toPath())
                    .filter(Files::isRegularFile)
                    .toList();

            for (Path sourceFile : sourceFiles) {
                Path relativePath = sourceDir.toPath().relativize(sourceFile);
                Path destFile = destDir.toPath().resolve(relativePath);

                if (!Files.exists(destFile)) {
                    return false; // File should exist in the destination directory
                }

                byte[] sourceBytes = Files.readAllBytes(sourceFile);
                byte[] destBytes = Files.readAllBytes(destFile);

                if (mode == Cipher.ENCRYPT_MODE) {
                    if (Arrays.equals(sourceBytes, destBytes)) {
                        return false; // Encrypted files should not be identical to the original files
                    }
                } else if (mode == Cipher.DECRYPT_MODE) {
                    if (!Arrays.equals(sourceBytes, destBytes)) {
                        return false; // Decrypted files should be identical to the original files
                    }
                }
            }
            return true; // All files processed correctly
        } catch (Exception e) {
            e.printStackTrace();
            return false; // If any exception occurs, return false
        }
    }
}

 */




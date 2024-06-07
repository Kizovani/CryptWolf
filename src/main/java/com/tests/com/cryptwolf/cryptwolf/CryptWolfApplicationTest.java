//TODO: PLEASE FIX THIS LMAO
/*
package com.cryptwolf.cryptwolf;

import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

public class CryptWolfApplicationTest extends ApplicationTest {

    private File sourceDir;
    private File destDir;
    private File tempFile;

    @Override
    public void start(Stage stage) throws Exception {
        new CryptWolfApplication().start(stage);
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Create temporary directories for testing
        sourceDir = Files.createTempDirectory("source").toFile();
        destDir = Files.createTempDirectory("dest").toFile();

        // Create a temporary file to encrypt/decrypt
        tempFile = new File(sourceDir, "testfile.txt");
        Files.write(tempFile.toPath(), "This is a test file.".getBytes());
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});

        // Delete temporary directories and files
        deleteDirectory(sourceDir);
        deleteDirectory(destDir);
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }

    @Test
    public void testEncryptAndDecryptWith256Bits() throws Exception {
        // Select 256 bits from the choice box
        clickOn("#keyLengthChoiceBox");
        clickOn("256 bits");

        // Select source and destination folders
        clickOn("#selectSourceButton");
        clickOn(sourceDir.getAbsolutePath());
        clickOn("OK");

        clickOn("#selectDestinationButton");
        clickOn(destDir.getAbsolutePath());
        clickOn("OK");

        // Click on the Encrypt button
        clickOn("#actionButton");
        verifyThat("#actionButton", hasText("Encrypt"));

        // Wait for encryption to complete (add a sleep if necessary for long processes)

        // Toggle to Decrypt mode
        clickOn("#toggleButton");
        verifyThat("#actionButton", hasText("Decrypt"));

        // Switch source and destination for decryption
        clickOn("#selectSourceButton");
        clickOn(destDir.getAbsolutePath());
        clickOn("OK");

        clickOn("#selectDestinationButton");
        clickOn(sourceDir.getAbsolutePath());
        clickOn("OK");

        // Click on the Decrypt button
        clickOn("#actionButton");
        verifyThat("#actionButton", hasText("Decrypt"));

        // Verify the decrypted file content
        Path decryptedFilePath = sourceDir.toPath().resolve("testfile.txt");
        String decryptedContent = new String(Files.readAllBytes(decryptedFilePath));
        assertEquals("This is a test file.", decryptedContent);
    }
}

 */


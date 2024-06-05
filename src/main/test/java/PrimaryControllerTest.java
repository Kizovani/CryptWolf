import com.cryptwolf.cryptwolf.PrimaryController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrimaryControllerTest {

    private PrimaryController controller;
    private SecretKey secretKey;

    @BeforeEach
    public void setUp() throws Exception {
        controller = new PrimaryController();
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // AES-256
        secretKey = keyGen.generateKey();
    }

    @Test
    public void testEncryptAndDecryptFile() throws Exception {
        // Create a temporary source file
        Path sourceFile = Files.createTempFile("testFile", ".txt");
        Files.write(sourceFile, "Hello, World!".getBytes());

        // Create a temporary destination file for encryption
        Path encryptedFile = Files.createTempFile("encryptedFile", ".enc");

        // Encrypt the file
        controller.processFile(sourceFile, encryptedFile, Cipher.ENCRYPT_MODE);

        // Create a temporary destination file for decryption
        Path decryptedFile = Files.createTempFile("decryptedFile", ".txt");

        // Decrypt the file
        controller.processFile(encryptedFile, decryptedFile, Cipher.DECRYPT_MODE);

        // Read the original and decrypted files
        byte[] originalContent = Files.readAllBytes(sourceFile);
        byte[] decryptedContent = Files.readAllBytes(decryptedFile);

        // Check if the original and decrypted content are the same
        assertArrayEquals(originalContent, decryptedContent);

        // Clean up temporary files
        Files.deleteIfExists(sourceFile);
        Files.deleteIfExists(encryptedFile);
        Files.deleteIfExists(decryptedFile);
    }

    @Test
    public void testProcessFiles() throws Exception {
        // Create a temporary source directory
        Path sourceDir = Files.createTempDirectory("sourceDir");
        Path sourceFile1 = sourceDir.resolve("file1.txt");
        Path sourceFile2 = sourceDir.resolve("file2.txt");
        Files.write(sourceFile1, "File 1 content".getBytes());
        Files.write(sourceFile2, "File 2 content".getBytes());

        // Create a temporary destination directory
        Path destDir = Files.createTempDirectory("destDir");

        // Encrypt the files
        controller.processFiles(sourceDir, destDir, Cipher.ENCRYPT_MODE);

        // Check if files are encrypted and exist in the destination directory
        Path encryptedFile1 = destDir.resolve(sourceDir.relativize(sourceFile1));
        Path encryptedFile2 = destDir.resolve(sourceDir.relativize(sourceFile2));
        assertTrue(Files.exists(encryptedFile1));
        assertTrue(Files.exists(encryptedFile2));

        // Decrypt the files back to the original directory
        controller.processFiles(destDir, sourceDir, Cipher.DECRYPT_MODE);

        // Read the decrypted files
        byte[] decryptedContent1 = Files.readAllBytes(sourceFile1);
        byte[] decryptedContent2 = Files.readAllBytes(sourceFile2);

        // Check if the original and decrypted content are the same
        assertArrayEquals("File 1 content".getBytes(), decryptedContent1);
        assertArrayEquals("File 2 content".getBytes(), decryptedContent2);

        // Clean up temporary files and directories
        Files.deleteIfExists(sourceFile1);
        Files.deleteIfExists(sourceFile2);
        Files.deleteIfExists(encryptedFile1);
        Files.deleteIfExists(encryptedFile2);
        Files.deleteIfExists(sourceDir);
        Files.deleteIfExists(destDir);
    }
}

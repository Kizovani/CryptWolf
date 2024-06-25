Hello! This is my small encryption/decryption app I made to learn about crypto and java/javaFX:

CryptWolf is a JavaFX-based application designed to securely encrypt and decrypt files using the AES (Advanced Encryption Standard) algorithm. It supports generating encryption keys, using passwords to create keys, and saving/loading keys from files, along with hopefully more key-management options in future releases.

Steps to Encrypt Files

    Select Source Directory:
        Click on the "Source Directory" button and choose the folder containing the files you want to encrypt.

    Select Destination Directory:
        Click on the "Destination Directory" button and choose the folder where you want the encrypted files to be saved.

    Choose Key Length:
        Use the "Key Length" dropdown to select the desired key length.

    Select Encryption Key Method:
        Enter a password in the password field to derive an encryption key or
        Check the "Use Key File" checkbox and select an existing key file to use for encryption or
        Let CryptWolf generate a new key.

    Save Key File (Optional):
        If a new key is generated, choose to save it by checking the "Save Key File" checkbox.

    Start Encryption:
        Click the "Encrypt" button to begin encrypting the files in the source directory. Monitor progress using the progress bar.

Steps to Decrypt Files

    Toggle to Decrypt Mode:
        Click the "Toggle Mode" button to switch to decryption mode.

    Select Source Directory:
        Click on the "Source Directory" button and choose the folder containing the encrypted files.

    Select Destination Directory:
        Click on the "Destination Directory" button and choose the folder where you want the decrypted files to be saved.

    Choose Key Length:
        Use the "Key Length" dropdown to select the key length that matches the encryption key length.

    Select Decryption Key Method:
        Enter the password used for encryption in the password field or
        Check the "Use Key File" checkbox and select the key file used for encryption.

    Start Decryption:
        Click the "Decrypt" button to begin decrypting the files in the source directory. Monitor progress using the progress bar.





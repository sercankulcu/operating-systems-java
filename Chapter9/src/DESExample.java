import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DESExample {
    public static void main(String[] args) throws Exception {
        // The message to be encrypted
        String message = "This is a secret message.";

        // Generate a secret key for the DES algorithm
        Key secretKey = new SecretKeySpec("mysecretkey12345".getBytes(), "DES");

        // Create a cipher object and initialize it for encrypting
        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Encrypt the message
        byte[] encryptedMessage = cipher.doFinal(message.getBytes());

        // Print the encrypted message (in base64 encoding)
        System.out.println("Encrypted message: " + Base64.getEncoder().encodeToString(encryptedMessage));

        // Initialize the cipher for decrypting
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        // Decrypt the encrypted message
        byte[] decryptedMessage = cipher.doFinal(encryptedMessage);

        // Print the decrypted message
        System.out.println("Decrypted message: " + new String(decryptedMessage));
    }
}

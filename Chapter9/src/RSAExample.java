import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;

/*
 * Here is an example of a Java program that demonstrates the use of the 
 * RSA (Rivest-Shamir-Adleman) algorithm for secure communication over the internet:
 * 
 * */

public class RSAExample {
    public static void main(String[] args) throws Exception {
        // Generate a key pair for the RSA algorithm
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        // The message to be encrypted
        String message = "This is a secret message.";

        // Create a cipher object and initialize it for encrypting
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        // Encrypt the message
        byte[] encryptedMessage = cipher.doFinal(message.getBytes());

        // Print the encrypted message (in base64 encoding)
        System.out.println("Encrypted message: " + Base64.getEncoder().encodeToString(encryptedMessage));

        // Initialize the cipher for decrypting
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        // Decrypt the encrypted message
        byte[] decryptedMessage = cipher.doFinal(encryptedMessage);

        // Print the decrypted message
        System.out.println("Decrypted message: " + new String(decryptedMessage));
    }
}

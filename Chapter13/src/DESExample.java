import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class DESExample {

	public static void main(String[] args) {
		try {
			// Generate a DES key
			KeyGenerator keyGen = KeyGenerator.getInstance("DES");
			SecureRandom secureRandom = new SecureRandom();
			keyGen.init(secureRandom);
			SecretKey secretKey = keyGen.generateKey();

			// Convert the key to a DESKeySpec
			byte[] keyBytes = secretKey.getEncoded();
			DESKeySpec desKeySpec = new DESKeySpec(keyBytes);
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey key = keyFactory.generateSecret(desKeySpec);

			// Initialize the cipher for encryption
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);

			// Encrypt the message
			String plainText = "This is a secret message.";
			byte[] plainTextBytes = plainText.getBytes("UTF8");
			byte[] encryptedBytes = cipher.doFinal(plainTextBytes);

			// Encode the encrypted bytes to a Base64 string
			String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);
			System.out.println("Encrypted text: " + encryptedText);

			// Initialize the cipher for decryption
			cipher.init(Cipher.DECRYPT_MODE, key);

			// Decode the encrypted bytes from the Base64 string
			byte[] decryptedBytes = Base64.getDecoder().decode(encryptedText);

			// Decrypt the message
			byte[] decryptedMessageBytes = cipher.doFinal(decryptedBytes);
			String decryptedText = new String(decryptedMessageBytes, "UTF8");
			System.out.println("Decrypted text: " + decryptedText);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

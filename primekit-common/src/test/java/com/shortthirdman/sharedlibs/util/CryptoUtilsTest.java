package com.shortthirdman.sharedlibs.util;

import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.KeyPair;
import java.util.Arrays;


public class CryptoUtilsTest {

    private static final String plainText = "Hello World";

    @Test
    public void testUsingSymmetricKey() throws Exception {
        SecretKey symmetricKey = CryptoUtils.generateKey("AES", 128);
        IvParameterSpec iv = CryptoUtils.generateIv();

        // Encrypt the message using the symmetric key
        final byte[] cipherText = CryptoUtils.encrypt("AES/CFB8/NoPadding", plainText, symmetricKey, iv);
        System.out.println("The encrypted message is: " + Arrays.toString(cipherText));

        // Decrypt the encrypted message
        final String decryptedText = CryptoUtils.decrypt("AES/CFB8/NoPadding", cipherText, symmetricKey, iv);
        System.out.println("Your original message is: " + decryptedText);
    }

    @Test
    public void testUsingKeyPair() throws Exception {
        final KeyPair keypair = CryptoUtils.generateRSAKKeyPair();
        final byte[] cipherText = CryptoUtils.encrypt(plainText, keypair.getPublic());

        final String hexFormattedValue = Hex.encodeHexString(cipherText);
        System.out.print("The encrypted text is: " + hexFormattedValue);

        final String decryptedText = CryptoUtils.decrypt(cipherText, keypair.getPrivate());
        System.out.println("The decrypted text is: " + decryptedText);
    }

    @Test
    public void testUsingDigitalSign() throws Exception {
        final KeyPair keypair = CryptoUtils.generateRSAKKeyPair();
        final byte[] digitalSignature = CryptoUtils.generateDigitalSignature(plainText.getBytes(), keypair.getPrivate());

        // If Java version > 17, use HexFormat.of().formatHex(digitalSignature)
        final String hexFormattedValue = Hex.encodeHexString(digitalSignature);
        System.out.println("Signature Value: " + hexFormattedValue);

        final boolean verifiedValue = CryptoUtils.verify(plainText.getBytes(), digitalSignature, keypair.getPublic());
        System.out.println("Verification: " + verifiedValue);
    }
}

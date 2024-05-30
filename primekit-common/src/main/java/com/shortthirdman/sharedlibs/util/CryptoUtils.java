package com.shortthirdman.sharedlibs.util;

import com.shortthirdman.sharedlibs.common.CryptoModes;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author shortthirdman
 * @since 1.0.0
 */
public class CryptoUtils {

    public static Set<String> listAllAlgorithms() {
        Set<String> algos = new TreeSet<>();

        for (Provider provider : Security.getProviders()) {
            Set<Provider.Service> service = provider.getServices();
            service.stream().map(Provider.Service::getAlgorithm).forEach(algos::add);
        }

        return algos;
    }

    public static int getBlockSize(final String algorithm) throws NoSuchPaddingException, NoSuchAlgorithmException {
        int size = 0;

        if (StringUtils.isBlank(algorithm)) {
            return size;
        }

        size = Cipher.getInstance(algorithm).getBlockSize();

        return size;
    }

    /**
     * @param algorithm the algorithm to generate key for
     * @param n the key length
     * @return the secret key
     * @throws NoSuchAlgorithmException
     */
    public static SecretKey generateKey(final String algorithm, final int n) throws NoSuchAlgorithmException {
        SecretKey key = null;

        if (algorithm == null || n <= 0) {
            return key;
        }

        KeyGenerator keygenerator = KeyGenerator.getInstance(algorithm);
        keygenerator.init(n);
        key = keygenerator.generateKey();

        return key;
    }

    public static IvParameterSpec generateIv() {
        byte[] initializationVector = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(initializationVector);
        return new IvParameterSpec(initializationVector);
    }

    /**
     * @param algorithm the Cipher algorithm
     * @param input the input plain text to encrypt
     * @param key the secret key
     * @param ivParamSpec the IV parameter spec
     * @return Encrypted content of the plain text
     * @throws Exception
     */
    public static byte[] encrypt(final String algorithm, final String input, final SecretKey key, IvParameterSpec ivParamSpec) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParamSpec);
        return cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * @param algorithm the Cipher algorithm
     * @param cipherText the input cipher text to decrypt
     * @param key the secret key
     * @param ivParamSpec the IV parameter spec
     * @return Decrypted content of the cipher encrypted text
     * @throws Exception
     */
    public static String decrypt(final String algorithm, final byte[] cipherText, final SecretKey key, final IvParameterSpec ivParamSpec) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, ivParamSpec);
        byte[] plainText = cipher.doFinal(cipherText);
        return new String(plainText);
    }

    /**
     * @return
     * @throws Exception
     */
    public static KeyPair generateRSAKKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(3072);
        return keyPairGenerator.generateKeyPair();
    }

    public static String decrypt(byte[] cipherText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] result = cipher.doFinal(cipherText);
        return new String(result);
    }

    public static byte[] encrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] generateDigitalSignature(byte[] plainText, PrivateKey privateKey) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] messageHash = md.digest(plainText);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return cipher.doFinal(messageHash);
    }

    public static boolean verify(byte[] plainText, byte[] digitalSignature, PublicKey publicKey) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedMessage = md.digest(plainText);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decryptedMessageHash = cipher.doFinal(digitalSignature);

        return Arrays.equals(decryptedMessageHash, hashedMessage);
    }

    /**
     *
     * @param secretKey
     *            Key used to encrypt data
     * @param plainText
     *            Text input to be encrypted
     * @return Returns encrypted text
     *
     */
    public static String encrypt(String secretKey, String plainText, byte[] salt, int keyCount)
            throws NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, keyCount);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, keyCount);

        Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        byte[] in = plainText.getBytes(StandardCharsets.UTF_8);
        byte[] out = ecipher.doFinal(in);
        return DatatypeConverter.printBase64Binary(out);
    }

    /**
     * @param secretKey     Key used to encrypt data
     * @param encryptedText Text input to be encrypted
     * @return Returns decrypted text
     */
    public static String decrypt(String secretKey, String encryptedText, byte[] salt, int keyCount) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, keyCount);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

        AlgorithmParameterSpec algoParamSpec = new PBEParameterSpec(salt, keyCount);

        Cipher dcipher = Cipher.getInstance(key.getAlgorithm());
        dcipher.init(2, key, algoParamSpec);
        byte[] enc = DatatypeConverter.parseBase64Binary(encryptedText);
        byte[] utf8 = dcipher.doFinal(enc);
        return new String(utf8, StandardCharsets.UTF_8);
    }

    /**
     * @param algorithm the algorithm name
     * @param key the secret key
     * @param iv the IV parameter secret key
     * @param inputFile the input file
     * @param outputFile the output encrypted file
     * @throws IOException
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static void encryptFile(String algorithm, SecretKey key, IvParameterSpec iv,
                                   File inputFile, File outputFile) throws IOException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        byte[] buffer = new byte[64];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                outputStream.write(output);
            }
        }
        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) {
            outputStream.write(outputBytes);
        }
        inputStream.close();
        outputStream.close();
    }

    /**
     * Encrypt the given serialized object with the specified algorithm
     * @param algorithm the algorithm name
     * @param object the object to encrypt
     * @param key the secret key
     * @param iv the IV parameter key
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws IOException
     * @throws IllegalBlockSizeException
     */
    public static SealedObject encryptObject(String algorithm, Serializable object,
                                             SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, IOException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        SealedObject sealedObject = new SealedObject(object, cipher);
        return sealedObject;
    }

    /**
     * @param algorithm
     * @param sealedObject
     * @param key
     * @param iv
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws ClassNotFoundException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws IOException
     */
    public static Serializable decryptObject(String algorithm, SealedObject sealedObject,
                                             SecretKey key, IvParameterSpec iv) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            ClassNotFoundException, BadPaddingException, IllegalBlockSizeException,
            IOException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        Serializable unsealObject = (Serializable) sealedObject.getObject(cipher);
        return unsealObject;
    }
}

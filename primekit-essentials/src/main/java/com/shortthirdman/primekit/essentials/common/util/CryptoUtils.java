package com.shortthirdman.primekit.essentials.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public final class CryptoUtils {

    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHA_NUMERIC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    private CryptoUtils() {
    }

    /**
     * @return set of all cryptographic algorithms
     */
    public static Set<String> listAllAlgorithms() {
        Set<String> algos = new TreeSet<>();

        for (Provider provider : Security.getProviders()) {
            Set<Provider.Service> service = provider.getServices();
            service.stream().map(Provider.Service::getAlgorithm).forEach(algos::add);
        }

        return algos;
    }

    /**
     * @param algorithm the algorithm name
     * @return the block size
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     */
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
        SealedObject sealedObject = null;

        if (algorithm != null && key != null && iv != null) {
            Cipher cipher = Cipher.getInstance(algorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            sealedObject = new SealedObject(object, cipher);
        }

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

    public static String getSalt(int length) {
        StringBuilder returnValue = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHA_NUMERIC.charAt(RANDOM.nextInt(ALPHA_NUMERIC.length())));
        }

        return new String(returnValue);
    }

    public static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);

        Arrays.fill(password, Character.MIN_VALUE);

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }
    }

    public static String generateSecurePassword(String password, String salt) {
        if (password == null || salt == null) {
            throw new IllegalArgumentException("");
        }
        String returnValue = null;

        byte[] securePassword = hash(password.toCharArray(), salt.getBytes());

        returnValue = Base64.getEncoder().encodeToString(securePassword);

        return returnValue;
    }

    public static boolean verifyUserPassword(String providedPassword, String securedPassword, String salt) {
        boolean returnValue = false;

        // Generate New secure password with the same salt
        String newSecurePassword = generateSecurePassword(providedPassword, salt);

        // Check if two passwords are equal
        returnValue = newSecurePassword.equalsIgnoreCase(securedPassword);

        return returnValue;
    }

    /**
     * @param input the input text to get SHA hash
     * @param algorithm the algorithm name
     * @return the message digest
     * @throws NoSuchAlgorithmException the exception for invalid algorithm
     */
    public static byte[] getSHA(String input, String algorithm) throws NoSuchAlgorithmException {
        if (input == null || algorithm == null) {
            throw new NoSuchAlgorithmException("");
        }

        MessageDigest md = MessageDigest.getInstance(algorithm);
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * @param hash the hash to convert
     * @return String
     */
    public static String toHexString(byte[] hash) {
        BigInteger number = new BigInteger(1, hash);

        StringBuilder hexString = new StringBuilder(number.toString(16));

        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }

    /**
     * @param digest
     * @return String
     */
    public static String toBase64String(byte[] digest) {
        return Base64.getEncoder().encodeToString(digest);
    }

    /**
     * Loads a private key with the specified org name from a keystore.
     *
     * @param keyStore keystore from which to load the private key
     * @param orgName name of the private key org
     * @param password keystore password
     * @return PrivateKey
     */
    public static PrivateKey loadKey(KeyStore keyStore, String password, String orgName) {
        try {
            PrivateKey key = (PrivateKey) keyStore.getKey(orgName, password.toCharArray());
            if (key == null) {
                throw new RuntimeException("Unable to get key for " + "name \"" + orgName + "\" using password \""
                        + password + "\" from keystore");
            }

            return key;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get a private key from the keystore by name and password.
     *
     * @param keystore keystore from which to load the private key
     * @param alias name of the private key alias
     * @param password keystore password
     * @return Key
     * @throws GeneralSecurityException
     */
    public static Key getKey(KeyStore keystore, String alias, String password) throws GeneralSecurityException {
        return keystore.getKey(alias, password.toCharArray());
    }

    /**
     * Loads a private key from input stream
     *
     * @param inStream the input stream to load key
     * @return Key
     */
    public static Key getKey(InputStream inStream) {
        try {
            ObjectInputStream ois = new ObjectInputStream(inStream);
            return (Key) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Gets the public key from the encoded byte.
     * The bytes can be recovered from a Hex string saved in a file etc.
     *
     * @param encodedKey the encoded public key in bytes
     * @param kfAlgorithm the key factory algorithm name
     * @return PublicKey
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static PublicKey getPublicKey(byte[] encodedKey, String kfAlgorithm) throws InvalidKeySpecException, NoSuchAlgorithmException {

        if (kfAlgorithm == null || kfAlgorithm.isEmpty()) {
            kfAlgorithm = "RSA";
        }

        X509EncodedKeySpec spec = new X509EncodedKeySpec(encodedKey);
        KeyFactory kf = KeyFactory.getInstance(kfAlgorithm);

        return kf.generatePublic(spec);
    }

    /**
     * @param modulus the modulus value of key
     * @param exponent the exponent value of key
     * @return RSAPublicKey
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static RSAPublicKey getPublicKey(BigInteger modulus, BigInteger exponent) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, exponent);

        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }
}

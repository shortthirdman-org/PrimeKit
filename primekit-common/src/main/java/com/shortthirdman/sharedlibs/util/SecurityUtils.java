package com.shortthirdman.sharedlibs.util;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class SecurityUtils {

    private SecurityUtils() {}

    /**
     * @param input
     * @param algorithm
     * @return byte[]
     * @throws NoSuchAlgorithmException
     */
    public static byte[] getSHA(String input, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * @param hash
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
            e.printStackTrace();
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

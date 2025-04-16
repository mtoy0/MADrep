package com.example.teamproject2;

import java.security.SecureRandom;
import java.util.Arrays;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import java.util.Base64;
import java.io.UnsupportedEncodingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public abstract class EncryptionMethods {
    //information and methods come from Professor Maxwell's security presentation and online resources
    //I am not too sure how it all works, but will do my best to explain

    //initialize variables
    public static SecretKeySpec secretKey;
    public static byte[] key;
    String secret = "A(&*&Niu87";

    //method to set key
    public static void setKey(String myKey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md = null;
        key = myKey.getBytes("UTF-8");
        md = MessageDigest.getInstance("SHA-512");
        key = md.digest(key);
        key = Arrays.copyOf(key, 16);
        secretKey = new SecretKeySpec(key, "AES");
    }
    //method to encrypt data using the provided key,
    public static String encrypt(String strToEncrypt, String secret) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        setKey(secret);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] strToEncryptBytes = strToEncrypt.getBytes("UTF-8");
        byte[] finalCipher = cipher.doFinal(strToEncryptBytes);
        return Base64.getEncoder().encodeToString(finalCipher);
    }

    //method to decrypt data using the same key
    public static String decrypt(String strToDecrypt, String secret) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        setKey(secret);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] finalByteString = Base64.getDecoder().decode(strToDecrypt);
        return new String(cipher.doFinal(finalByteString));
    }

    //generates salt for password encryption
    public static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    //hashes password using salt
    public static String hashPassword(String passwordToHash, String saltString) throws NoSuchAlgorithmException {
        String generatedPassword = null;
        byte[] salt = Base64.getDecoder().decode(saltString);
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        byte[] bytes = md.digest(passwordToHash.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(String.format("%02x", aByte));
        }
        generatedPassword = sb.toString();

        return generatedPassword;
    }
}

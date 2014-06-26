package ru.giperball.qrpassword.app;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * This class encrypt and decrypt strings to string with some algorithm
 */
public class Encryptor {
    private static final String CRYPTO_ALGORITHM = "PBEWithMD5AndDES";
    private static final String STRING_ENCODING = "UTF-8";

    public static String encrypt(String rawString, String password) throws Exception {
        Cipher cipher = getCipher(password, Cipher.ENCRYPT_MODE);
        byte[] rawData = rawString.getBytes(STRING_ENCODING);
        byte[] encryptedData = cipher.doFinal(rawData);
        return Base64.encodeToString(encryptedData, Base64.DEFAULT);
    }

    public static String decrypt(String encrypedString, String password) throws Exception {
        Cipher cipher = getCipher(password, Cipher.DECRYPT_MODE);
        byte[] encryptedData = Base64.decode(encrypedString, Base64.DEFAULT);
        byte[] rawData = cipher.doFinal(encryptedData);
        return new String(rawData, STRING_ENCODING);
    }

    private static Cipher getCipher(String password, int mode) throws Exception {
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(CRYPTO_ALGORITHM);
        SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
        byte[] salt = password.getBytes(STRING_ENCODING);
        int count = password.length();
        PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(salt, count);
        Cipher cipher = Cipher.getInstance(CRYPTO_ALGORITHM);
        cipher.init(mode, secretKey, pbeParameterSpec);
        return cipher;
    }
}

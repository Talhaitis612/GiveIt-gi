package com.example.giveit_gi.Utils;

import se.simbio.encryption.Encryption;

public class PasswordEncryptionDecryption {
    private static String KEY = "fYcNwTf2gU";
    private static String SALT = "/-_#*+!?()=:.@";
    private static byte[] IV = new byte[16];
    private static Encryption encryption = Encryption.getDefault(KEY, SALT, IV);


    public static String encryptPassword(String password) {
        String encryptedPassword = encryption.encryptOrNull(password);
        return encryptedPassword;
    }

    public static String decryptPassword(String password) {
        String decryptedPassword = encryption.encryptOrNull(password);
        return decryptedPassword;
    }


}

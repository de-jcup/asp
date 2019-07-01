package de.jcup.asp.core;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CryptoAccess {
    private static final String SECURITY_ALGORITHM = "AES";
    private static KeyGenerator keyGen;
    private SecretKey secretKey;

    /**
     * Creates a new crypto object, AES algorithm used, secret key will be generated
     */
    public CryptoAccess() {
        secretKey = getkeyGen(SECURITY_ALGORITHM).generateKey();
    }

    /**
     * Create crypto object, using given base 64 encoded key
     * @param encodedKey, bas64 encoded secret key
     */
    public CryptoAccess(String base64encodedSecretKey) {
        byte[] decodedKey = Base64.getDecoder().decode(base64encodedSecretKey);
        secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, SECURITY_ALGORITHM);
    }

    /**
     * @return secret key, base64 encoded 
     */
    public String getSecretKey() {
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        return encodedKey;
    }

    private static KeyGenerator getkeyGen(String algorithm) {
        if (CryptoAccess.keyGen != null) {
            return CryptoAccess.keyGen;
        }
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
            keyGen.init(128);
            CryptoAccess.keyGen = keyGen;
            return CryptoAccess.keyGen;

        } catch (Exception e) {
            throw new IllegalStateException("FATAL:cannot create key generator", e);
        }
    }

    public String encrypt(String strToEncrypt) {
        try {
            Cipher cipher = Cipher.getInstance(SECURITY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new SecureRandom());
            byte[] encryptedBytes = cipher.doFinal(strToEncrypt.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encryptedBytes);
            
        } catch (Exception e) {
            throw new IllegalStateException("cannot encrypt given string", e);
        }
    }

    public String decrypt(String strToDecrypt) throws DecryptionException{
        try {
            Cipher cipher = Cipher.getInstance(SECURITY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] base64ToDecrypt = Base64.getDecoder().decode(strToDecrypt);
            byte[] decryptedBytes = cipher.doFinal(base64ToDecrypt);
            return new String(decryptedBytes);
        } catch (Exception e) {
            if (e instanceof BadPaddingException) {
                /* bad key detected */
                throw new DecryptionException("Decryption failed, bad key",e);
            }
            throw new IllegalStateException("cannot decrypt given string", e);
        }
    }
    
    public class DecryptionException extends Exception{

        private static final long serialVersionUID = 1L;
        
        public DecryptionException(String message, Throwable t) {
            super(message,t);
        }
        
    }

}
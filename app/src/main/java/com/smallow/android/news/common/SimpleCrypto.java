package com.smallow.android.news.common;

import android.util.Base64;

import com.smallow.android.news.SystemConst;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by smallow on 2015/2/9.
 */
public class SimpleCrypto {
    private static byte[] iv = SystemConst.VECTOR.getBytes();
    private static IvParameterSpec ips = new IvParameterSpec(iv);

    public static String encrypt(String cleartext) throws Exception {
        return encrypt(SystemConst.ENCRYPTO_KEY, cleartext);
    }

    public static String decrypt(String encrypted) throws Exception {
        return decrypt(SystemConst.ENCRYPTO_KEY, encrypted);
    }

    public static String encrypt(String seed, String cleartext)
            throws Exception {
        // byte[] rawKey = getRawKey(seed.getBytes());
        byte[] rawKey = seed.getBytes();
        byte[] result = encrypt(rawKey, cleartext.getBytes());
        //return toHex(result);
        return Base64.encodeToString(result, Base64.DEFAULT);
    }

    public static String decrypt(String seed, String encrypted)
            throws Exception {
        // byte[] rawKey = getRawKey(seed.getBytes());
        byte[] rawKey = seed.getBytes();
        //byte[] enc = toByte(encrypted);
        byte[] enc = Base64.decode(encrypted, Base64.DEFAULT);
        byte[] result = decrypt(rawKey, enc);
        return new String(result);
    }


    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, 0, 32, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ips);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted)
            throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, 0, 32, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, ips);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }


    public static byte[] getRawKey(byte[] seed) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        kgen.init(128, sr);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }


    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        if (hexString == null)
            return "".getBytes();
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; ++i) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                    16).byteValue();
        }
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null) {
            return "";
        }
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private final static String HEX = "01234567890ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }
}

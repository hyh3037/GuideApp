package com.jyyl.guideapp.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {

    private MD5Utils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }
    
    public static String MD5Encode(byte[] toencode) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(toencode);
            return HexEncode(md5.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
 
    public static String HexEncode(byte[] toencode) {
        StringBuilder sb = new StringBuilder(toencode.length * 2);
        for (byte b : toencode) {
            sb.append(Integer.toHexString((b & 0xf0) >>> 4));
            sb.append(Integer.toHexString(b & 0x0f));
        }
        return sb.toString();
    }

    /**
     * MD5转换方法
     * @param sourceStr  加密对象
     * @return String    加密后的字符串
     */
    public static String toMd5(String sourceStr) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(sourceStr.getBytes());
            int i;
            StringBuilder buf = new StringBuilder();
            byte[] b = algorithm.digest();

            for (byte aB : b) {
                i = aB;

                if (i < 0)
                    i += 256;

                if (i < 16)
                    buf.append("0");

                buf.append(Integer.toHexString(i));

            }

            return buf.toString();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

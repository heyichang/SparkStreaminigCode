package com.ceiec.bigdata.util;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by heyichang on 2017/10/13.
 */
public class InfoIdUtils {

    /**Determine encrypt algorithm MD5*/
    private static final String ALGORITHM_MD5 = "MD5";
    /**UTF-8 Encoding*/
    private static final String UTF_8 = "UTF-8";

    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public static String get32UUID() {

        return UUID.randomUUID().toString().replace("-", "");
    }



    /**
     * 对字符串进行MD5编码
     * @param originString
     * @return
     */
    public static String encodeByMD5(String originString) {
        if (originString != null){
            try {
                MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);
                byte[] results = md.digest(originString .getBytes());
                String resultString = byteArrayToHexString(results);
                return resultString.toUpperCase();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     *对字符串进行MD5编码
     * @param input
     * @return
     */
    public static String generate32MD5ID(String input) {
        try {
            byte[] bytes = MessageDigest.getInstance("MD5").digest(input.getBytes("utf-8")); //128位的二进制数组
            //将128位的二进制数组转换为32位的16进制字符串
            StringBuffer md5Str = new StringBuffer();
            int temp;
            for (byte _byte : bytes) {
                temp = _byte;
                if (temp < 0) {
                    temp += 256;
                }
                if (temp < 16) {
                    md5Str.append("0");
                }
                md5Str.append(Integer.toHexString(temp));
            }
            return md5Str.toString().toUpperCase();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }



    /**
     * 转换字节数组为16进制字串
     *
     * @param b  字节数组
     * @return 十六进制字串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
     * 将一个字节转化成16进制形式的字符串
     * @param b
     * @return
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }



    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {

    }



}

package com.ceiec.graph.utils;

import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.MD5Hash;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.UUID;

/**
 * Created by zoumengcheng on 2017/10/13.
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


    public static final String MD5_64bit() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        byte [] rowkey = Bytes.add(MD5Hash.getMD5AsHex(Bytes.toBytes(Long.valueOf(sb.toString()))).substring(0, 8).getBytes(),
                Bytes.toBytes(Long.valueOf(sb.toString())));

        String readyEncryptStr = sb.toString();
        //System.out.println(readyEncryptStr);
        MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(md.digest(readyEncryptStr.getBytes(UTF_8)));
    }


    public static final String getMD564bit(String sb) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        byte [] rowkey = Bytes.add(MD5Hash.getMD5AsHex(Bytes.toBytes(Long.valueOf(sb.toString()))).substring(0, 8).getBytes(),
                Bytes.toBytes(Long.valueOf(sb.toString())));

        String readyEncryptStr = sb.toString();
        //System.out.println(readyEncryptStr);
        MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encode(md.digest(readyEncryptStr.getBytes(UTF_8)));
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
        Random r = new Random();
        StringBuilder sb = new StringBuilder(16);
        sb.append(r.nextInt(99999999)).append(r.nextInt(99999999));
        int len = sb.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sb.append("0");
            }
        }
        byte [] rowkey = Bytes.add(MD5Hash.getMD5AsHex(Bytes.toBytes(Long.valueOf(sb.toString()))).substring(0, 8).getBytes(),
                Bytes.toBytes(Long.valueOf(sb.toString())));
        System.out.println(MD5_64bit());
        System.out.println(Long.valueOf(MD5_64bit().hashCode()));
        System.out.println(rowkey);
        for (int i=0;i<20;i++)
        System.out.println(get32UUID());
        System.out.println(getMD564bit("910341290675412996"));
    }



}

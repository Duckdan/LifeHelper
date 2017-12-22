package com.study.yang.lifehelper.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *         加密工具类，此类利用MD5算法进行加密
 */
public class PassWordUtils {

    public static String getMD5(String pwd) {
        // TODO Auto-generated method stub
        String encryptPwd = "";
        try {
            //构建md对象
            MessageDigest md = MessageDigest.getInstance("md5");
            //对密码进行加盐算法
            byte[] digest = md.digest((pwd + "yangminghan").getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digest.length; i++) {
                byte b = digest[i];
                //将负数转化为整数
                int single = b & 0xFF;
                //将int类型的整数转化成16进制的数
                String hexString = Integer.toHexString(single);
                //为了让加密之后的结果产生一个32位的字符串
                if (hexString.length() == 1) {
                    hexString = 0 + hexString;
                }
                sb.append(hexString);
            }
            encryptPwd = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return encryptPwd;
    }

}

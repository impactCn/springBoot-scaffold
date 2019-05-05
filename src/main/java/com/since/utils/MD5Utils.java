package com.since.utils;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description: MD5加密的工具类，慎修改，md5加密不可逆转
 * @author: SY_zheng
 * @create: 2019-05-05
 */
public class MD5Utils {

    /**
     * 进行md5加密
     * @param password
     * @return md5加密后的密码
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static String encoderByMd5(String password) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        // 确定计算方法
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        // 加密后的字符串
        String md5Password = base64Encoder.encode(md5.digest(password.getBytes("utf-8")));
        return md5Password;
    }

    /**
     * 检查未加密的密码是否与加密的密码一致
     * @param password 未加密的密码
     * @param md5Password 加密后的密码
     * @return
     * @throws NoSuchAlgorithmException
     * @throws UnsupportedEncodingException
     */
    public static boolean checkPassword(String password, String md5Password) throws NoSuchAlgorithmException, UnsupportedEncodingException  {
        return encoderByMd5(password).equals(md5Password)? true: false;
    }
}
package org.jts.util;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

public class JasyptEncryptorUtils {

    /**
     * 加密
     * @param secretKey 密钥
     * @param value 要加密字符串
     * @return 加密字符串
     */
    public static String encryptPwd(String secretKey, String value) {
        PooledPBEStringEncryptor encryptOr = new PooledPBEStringEncryptor();
        encryptOr.setConfig(cryptOr(secretKey));
        return encryptOr.encrypt(value);
    }

    /**
     * 解密
     * @param secretKey 密钥
     * @param value 加密字符串
     * @return 解密字符串
     */
    public static String decyptPwd(String secretKey, String value) {
        PooledPBEStringEncryptor encryptOr = new PooledPBEStringEncryptor();
        encryptOr.setConfig(cryptOr(secretKey));
        return encryptOr.decrypt(value);
    }

    /**
     * 配置
     * @param secretKey 密钥
     * @return 配置
     */
    private static SimpleStringPBEConfig cryptOr(String secretKey) {
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(secretKey);
        // 加密算法
        config.setAlgorithm("PBEWithMD5AndDES");
        // 循环次数
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");
        config.setProviderName(null);
        // 盐
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setStringOutputType("base64");
        config.setIvGeneratorClassName("org.jasypt.salt.NoOpIVGenerator");
        return config;
    }

    public static void main(String[] args) {
        String secretKey = "EbfYkitulv73I2p0mXI50JMXoaxZTKJ7";
        String encryptPwd = encryptPwd(secretKey, "YLKPB123456");
        System.out.printf("Encrypted password [%s]%n",encryptPwd);
    }
}

package com.github.ylyan2015.util;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Security;

/**
 * 国密算法工具类
 */
@Component
@Slf4j
public class SmCryptoUtil {

    @PostConstruct
    public void init() {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * SM3哈希算法 - 用于密码加密
     */
    public String sm3Hash(String input) {
        if (input == null) {
            return null;
        }
        
        try {
            byte[] inputBytes = input.getBytes("UTF-8");
            SM3Digest digest = new SM3Digest();
            digest.update(inputBytes, 0, inputBytes.length);
            
            byte[] result = new byte[digest.getDigestSize()];
            digest.doFinal(result, 0);
            
            return bytesToHex(result);
        } catch (Exception e) {
            log.error("SM3哈希计算失败", e);
            throw new RuntimeException("密码加密失败", e);
        }
    }

    /**
     * SM3加盐哈希 - 更安全的密码加密方式
     */
    public String sm3HashWithSalt(String input, String salt) {
        if (input == null || salt == null) {
            return null;
        }
        
        try {
            String combined = input + salt;
            byte[] inputBytes = combined.getBytes("UTF-8");
            SM3Digest digest = new SM3Digest();
            digest.update(inputBytes, 0, inputBytes.length);
            
            byte[] result = new byte[digest.getDigestSize()];
            digest.doFinal(result, 0);
            
            return bytesToHex(result);
        } catch (Exception e) {
            log.error("SM3加盐哈希计算失败", e);
            throw new RuntimeException("密码加密失败", e);
        }
    }

    /**
     * 生成随机盐值
     */
    public String generateSalt() {
        try {
            byte[] salt = new byte[16];
            java.security.SecureRandom random = java.security.SecureRandom.getInstanceStrong();
            random.nextBytes(salt);
            return bytesToHex(salt);
        } catch (Exception e) {
            log.error("生成盐值失败", e);
            throw new RuntimeException("生成盐值失败", e);
        }
    }

    /**
     * 字节数组转十六进制字符串
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}

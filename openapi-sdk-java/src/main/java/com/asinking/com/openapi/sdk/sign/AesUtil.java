package com.asinking.com.openapi.sdk.sign;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AesUtil {

    private static final Logger logger = LoggerFactory.getLogger(AesUtil.class);

    private AesUtil() {
    }

    private static final String ECB_MODE = "AES/ECB/PKCS5PADDING";

    private static final String PREFIX = "ak_";

    private static final String[] CHARSET = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c",
            "d", "e", "f", "g", "h", "i", "g", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    /**
     * <p>
     * 生成16位的appKey
     * </p>
     *
     * @return appKey
     */
    public static String getAppKey() {
        List<String> list = Arrays.asList(CHARSET);
        // 打乱排序
        Collections.shuffle(list);
        StringBuilder builder = new StringBuilder();
        builder.append(PREFIX);
        for (int index = 0; index < 13; index++) {
            builder.append(list.get(SECURE_RANDOM.nextInt(list.size())));
        }
        return builder.toString();
    }

    /**
     * 取4位随机数
     *
     * @return random
     */
    public static String getRandom(int pos) {
        List<String> list = Arrays.asList(CHARSET);
        // 打乱排序
        Collections.shuffle(list);
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < pos; index++) {
            builder.append(list.get(SECURE_RANDOM.nextInt(list.size())));
        }
        return builder.toString();
    }

    /**
     * <p>
     * aes加密采用ecb模式，填充方式为pkcs5padding
     * 其中cbc模式需要有向量iv
     * </p>
     *
     * @param password 密码
     * @param appKey   生成的16位appKey
     * @return aes加密的密码
     */
    public static String encryptEcb(String password, String appKey) {
        try {
            SecretKeySpec spec = new SecretKeySpec(appKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(ECB_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, spec);
            byte[] encrypted = cipher.doFinal(password.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            logger.error("encrypt ecb error.", ex);
        }
        return "";
    }

    /**
     * aes ecb解密
     *
     * @param encrypted aes ecb加密后的密码
     * @param appKey    生成的16位appKey
     * @return password
     */
    public static String decryptEcb(String encrypted, String appKey) {
        try {
            SecretKeySpec spec = new SecretKeySpec(appKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(ECB_MODE);
            cipher.init(Cipher.DECRYPT_MODE, spec);
            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));
            return new String(original);
        } catch (Exception ex) {
            logger.error("decrypt ecb error.", ex);
        }
        return "";
    }
}

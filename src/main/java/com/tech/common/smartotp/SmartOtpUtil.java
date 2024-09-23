package com.tech.common.smartotp;


import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.UUID;

/**
 * The type Smart otp util.
 */
@Component
public class SmartOtpUtil {

    /**
     * Generate secret key string.
     *
     * @return the string
     */
    public String generateSecretKey() {
        String userId = "1";
        String deviceId = "1";
        String key = MessageFormat.format("{0}-{1}-{2}", userId, deviceId, UUID.randomUUID().toString());
        return encodeBase64(key);
    }

    /**
     * Verify smart otp boolean.
     *
     * @param secretKeyEncoded the secret key encoded
     * @param otpCode          the otp code
     * @return the boolean
     */
    public boolean verifySmartOtp(String secretKeyEncoded, String otpCode) {
        int bufferTime = 5;
        for (int i = -bufferTime; i <= bufferTime; i++) {
            String otp = generateTOTP(secretKeyEncoded, i);
            return otp.equals(otpCode);
        }
        return false;
    }

    /**
     * Generate totp string.
     *
     * @param secretKeyEncoded the secret key encoded
     * @param bufferSeconds    the buffer seconds
     * @return the string
     */
    public String generateTOTP(String secretKeyEncoded, long bufferSeconds) {
        int timePeriod = 30;
        try {
            String secretKey = decodeBase64(secretKeyEncoded);
            long timeToSeconds = System.currentTimeMillis() / 1000;
            long timeInterval = (timeToSeconds + bufferSeconds) / timePeriod;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] hash = mac.doFinal(String.valueOf(timeInterval).getBytes());
            int hashCode = Math.abs(DatatypeConverter.printHexBinary(hash).hashCode());
            String otp = String.format("%06d", hashCode);
            if (otp.length() > 6) {
                return otp.substring(0, 6);
            }
            return otp;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Encode base 64 string.
     *
     * @param encodeString the encode string
     * @return the string
     */
    public String encodeBase64(String encodeString) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(encodeString.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decode base 64 string.
     *
     * @param input the input
     * @return the string
     */
    public String decodeBase64(String input) {
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(input), StandardCharsets.UTF_8);
//        return new String(decoder.decode(input), StandardCharsets.UTF_8).replaceAll("=", "");
    }
}

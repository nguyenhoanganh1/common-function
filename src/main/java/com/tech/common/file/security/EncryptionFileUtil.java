package com.tech.common.file.security;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Dùng để mã hoá file
 */
public class EncryptionFileUtil {


    /**
     * Dùng để mã hoá file. khi download về hoặc xem trực tiếp sẽ không nhìn thấy
     *
     * @param inputStream the input stream
     * @return input stream
     */
    public static InputStream encrypt(InputStream inputStream) {
        try {
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);

            int key = pixelMask();
            int i = 0;
            for (byte b : data) {
                data[i] = (byte) (b ^ key);
                i++;
            }
            return new ByteArrayInputStream(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Pixel mask int.
     *
     * @return the int
     */
    public static int pixelMask() {
        int a = 1;
        int r = 255;
        int g = 255;
        int b = 255;
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

}

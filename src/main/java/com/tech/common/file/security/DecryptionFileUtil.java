package com.tech.common.file.security;

import java.io.*;

import static com.tech.common.file.security.EncryptionFileUtil.pixelMask;

/**
 * The type Description file util.
 */
public class DecryptionFileUtil {

    /**
     * Dùng để dịch ngược lại file bị mã hoá
     *
     * @param imagePath the image path
     * @return input stream
     */
    public static InputStream decrypt(String imagePath) {
        byte[] data;
        try (InputStream inputStream = new FileInputStream(imagePath)) {
            data = new byte[inputStream.available()];
            inputStream.read(data);

            int key = pixelMask();
            int i = 0;
            for (byte b : data) {
                data[i] = (byte) (b ^ key);
                i++;
            }
            return new ByteArrayInputStream(data);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Dùng để dịch ngược lại file bị mã hoá
     *
     * @param data the data
     * @return input stream
     */
    public static InputStream revertFile(byte[] data) {
        try {
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
}

package com.tech.common.file.csv.importer;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * The type Abstract export csv.
 *
 * @param <T> the type parameter
 */
public abstract class BaseImportCsv<T> {

    private final Set<T> processedData = new HashSet<>(); // Lưu trữ các dữ liệu đã xử lý
    private final List<T> duplicateData = new ArrayList<>(); // Lưu trữ các dòng trùng lặp

    /**
     * Gets headers.
     *
     * @return the headers
     */
    protected abstract List<String> getHeaders();

    /**
     * Validate header boolean.
     *
     * @return the boolean
     */
    protected abstract boolean isSkipHeader();

    /**
     * Check duplicate data boolean.
     *
     * @param processedData the processed data
     * @param object        the object
     * @return the boolean
     */
    protected abstract boolean checkDuplicateData(Set<T> processedData, T object);

    /**
     * Convert line to object t.
     *
     * @param line the line
     * @return the t
     */
    protected abstract T convertLineToObject(String line);

    /**
     * Convert line to object t.
     *
     * @param processedData the processed data
     * @return the t
     */
    protected abstract List<T> saveData(Set<T> processedData);

    /**
     * Upload duplicate data.
     *
     * @param duplicateData the duplicate data
     */
    protected abstract void uploadDuplicateData(List<T> duplicateData);

    /**
     * Validate file boolean.
     *
     * @param file the file
     * @return the boolean
     */
    public boolean validateFile(MultipartFile file) {
        // Kiểm tra xem file có rỗng không
        if (file.isEmpty()) {
            System.out.println("Uploaded file is empty");
            return false;
        }

        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("text/csv") && !contentType.equals("application/vnd.ms-excel"))) {
            System.out.println("Invalid file format");
            return false;
        }

        // Check kích thước file (ví dụ: không quá 10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            System.out.println("File is too large");
            return false;
        }
        return true;
    }

    /**
     * Import file.
     *
     * @param file      the file
     * @param chunkSize the chunk size
     * @throws IOException the io exception
     */
    public boolean importFile(MultipartFile file, int chunkSize) {
        if (!validateFile(file)) {
            throw new IllegalArgumentException("Invalid file");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            this.processHeader(reader);

            List<String> chunk = new ArrayList<>();
            String line;

            while (Objects.nonNull(line = reader.readLine())) {
                chunk.add(line);

                if (chunk.size() == chunkSize) {
                    List<String> currentChunk = new ArrayList<>(chunk);
                    this.processChunk(currentChunk);
                    chunk.clear();
                }
            }

            this.uploadDuplicateData(duplicateData);

            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            duplicateData.clear();
            processedData.clear();
        }
    }

    private void processHeader(BufferedReader reader) throws IOException {
        if (isSkipHeader()) {
            reader.readLine(); // Bỏ qua dòng đầu tiên
        } else {
            String headerLine = reader.readLine();
            List<String> fileHeader = Arrays.asList(headerLine.split(","));

            // Kiểm tra tất cả tên cột trong file có giống trong hệ thống hay không?
            if (!validateHeader(fileHeader)) {
                throw new IllegalArgumentException("Header does not contain required columns");
            }
        }
    }

    /**
     * Phương thức xử lý từng chunk, kiểm tra trùng lặp
     *
     * @param chunk the chunk
     */
    protected void processChunk(List<String> chunk) {
        for (String line : chunk) {
            T object = convertLineToObject(line); // Chuyển đổi dòng thành đối tượng
            if (checkDuplicateData(processedData, object)) {
                // Nếu trùng, thêm vào danh sách dữ liệu trùng lặp
                duplicateData.add(object);
            }

            processedData.add(object);
        }

        this.saveData(processedData);
    }

    /**
     * Validate header boolean.
     *
     * @param fileHeader the header
     * @return the boolean
     */
    protected boolean validateHeader(List<String> fileHeader) {
        // Kiểm tra xem header có chứa tất cả các cột cần thiết không
        if (!fileHeader.containsAll(this.getHeaders())) {
            System.out.println("Header does not contain all required columns");
            return false;
        }

        // Kiểm tra xem header có cột không cần thiết không
        Set<String> headerSet = new HashSet<>(fileHeader);
        Set<String> requiredHeaderSet = new HashSet<>(this.getHeaders());

        if (!headerSet.equals(requiredHeaderSet)) {
            System.out.println("Header contains invalid columns");
            return false;
        }

        return true;
    }
}

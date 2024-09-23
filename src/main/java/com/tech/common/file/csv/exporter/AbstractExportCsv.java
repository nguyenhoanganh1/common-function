package com.tech.common.file.csv.exporter;

import com.opencsv.CSVWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * The type Abstract export csv.
 *
 * @param <T> the type parameter
 */
public abstract class AbstractExportCsv<T> {

    /**
     * Is write header boolean.
     *
     * @return the boolean
     */
    protected abstract boolean isWriteHeader();

    /**
     * Is number order boolean.
     *
     * @return the boolean
     */
    protected abstract boolean isNumberOrder();

    /**
     * Gets file name.
     *
     * @return the file name
     */
    protected abstract String getFileName();

    /**
     * Get header string [ ].
     *
     * @return the string [ ]
     */
    protected abstract String[] getHeader();

    /**
     * Convert to row string [ ].
     *
     * @param item  the item
     * @param index the index
     * @return the string [ ]
     */
    protected abstract String[] convertToRow(T item, int index);

    public byte[] export(List<T> data) {
        // Ghi File có dấu
        byte[] boms = new byte[]{
                Integer.decode("0xef").byteValue(),
                Integer.decode("0xbb").byteValue(),
                Integer.decode("0xbf").byteValue()
        };
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream))) {
            outputStream.write(boms);
            if (isWriteHeader()) {
                writeHeader(writer);
            }
            writeDataInChunks(writer, data);
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
        return outputStream.toByteArray();
    }

    private void writeHeader(CSVWriter writer) throws IOException {
        String[] header = getHeader();
        if (isNumberOrder() && Objects.equals(header[0], "STT")) {
            header = new String[header.length - 1];
            System.arraycopy(header, 1, header, 0, header.length - 1);
        }
        writer.writeNext(header);
    }

    private void writeDataInChunks(CSVWriter writer, List<T> data) throws IOException {
        int chunkSize = 1000;
        int totalChunks = (data.size() + chunkSize - 1) / chunkSize;
        CompletableFuture<?>[] futures = new CompletableFuture[totalChunks];

        for (int i = 0; i < totalChunks; i++) {
            int start = i * chunkSize;
            int end = Math.min(start + chunkSize, data.size());

            futures[i] = CompletableFuture.runAsync(() -> {
                try {
                    writeChunk(writer, data.subList(start, end), start);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        CompletableFuture.allOf(futures).join();

    }

    private void writeChunk(CSVWriter writer, List<T> chunk, int startIndex) throws IOException {
        for (int i = 0; i < chunk.size(); i++) {
            String[] row = convertToRow(chunk.get(i), startIndex + i + 1);
            if (isNumberOrder()) {
                row = new String[row.length - 1];
                System.arraycopy(row, 1, row, 0, row.length - 1);
            }
            writer.writeNext(row);
        }
        writer.flush();
    }
}

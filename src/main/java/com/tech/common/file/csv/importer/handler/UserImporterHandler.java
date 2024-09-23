package com.tech.common.file.csv.importer.handler;

import com.tech.common.file.csv.dto.UserDto;
import com.tech.common.file.csv.importer.AbstractImportCsv;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * The type User importer handler.
 */
@Component
public class UserImporterHandler extends AbstractImportCsv<UserDto> {

    @Override
    protected List<String> getHeaders() {
        return List.of();
    }

    @Override
    protected boolean isSkipHeader() {
        return false;
    }


    @Override
    protected boolean checkDuplicateData(Set<UserDto> processedData, UserDto object) {
        // Có thể check duplicate dựa trên 1 key duy nhất
        return processedData.contains(object);
    }


    @Override
    protected UserDto convertLineToObject(String line) {
        return null;
    }

    @Override
    protected List<UserDto> saveData(Set<UserDto> processedData) {
        // lưu vào database
        return List.of();
    }

    @Override
    protected void uploadDuplicateData(List<UserDto> duplicateData) {

    }

}

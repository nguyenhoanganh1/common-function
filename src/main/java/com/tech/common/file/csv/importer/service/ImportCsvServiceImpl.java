package com.tech.common.file.csv.importer.service;

import com.tech.common.file.csv.importer.handler.UserImporterHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ImportCsvServiceImpl implements ImportCsvService {

    private final UserImporterHandler userImporterHandler;

    @Override
    public Boolean importUserCsv(MultipartFile file) {
        int chunk = 1000;
        return userImporterHandler.importFile(file, chunk);
    }
}

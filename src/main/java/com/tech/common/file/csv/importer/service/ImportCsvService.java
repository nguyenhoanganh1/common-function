package com.tech.common.file.csv.importer.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImportCsvService {

    Boolean importUserCsv(MultipartFile file);

}

package com.tech.common.file.csv.exporter.service;


import com.tech.common.file.csv.dto.ProductDto;
import com.tech.common.file.csv.dto.UserDto;
import com.tech.common.file.csv.exporter.handler.ProductCsvExportHandler;
import com.tech.common.file.csv.exporter.handler.UserCsvExportHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The type Export service.
 */
@RequiredArgsConstructor
@Service
public class ExportServiceImpl implements ExportCsvService {

    private final UserCsvExportHandler userCsvExportHandler;
    private final ProductCsvExportHandler productCsvExportHandler;

    @Override
    public byte[] exportUser(List<UserDto> data) {
        return userCsvExportHandler.export(data);
    }

    @Override
    public byte[] exportProduct(List<ProductDto> data) {
        return productCsvExportHandler.export(data);
    }
}

package com.tech.common.file.csv.exporter.handler;

import com.tech.common.file.csv.dto.ProductDto;
import com.tech.common.file.csv.exporter.AbstractExportCsv;
import org.springframework.stereotype.Component;

@Component
public class ProductCsvExportHandler extends AbstractExportCsv<ProductDto> {

    @Override
    protected boolean isWriteHeader() {
        return false;
    }

    @Override
    protected boolean isNumberOrder() {
        return false;
    }

    @Override
    protected String getFileName() {
        return "";
    }

    @Override
    protected String[] getHeader() {
        return new String[]{"STT", "Id", "Name"};
    }

    @Override
    protected String[] convertToRow(ProductDto item, int index) {
        return new String[]{String.valueOf(index), item.getId(), item.getName()};
    }
}

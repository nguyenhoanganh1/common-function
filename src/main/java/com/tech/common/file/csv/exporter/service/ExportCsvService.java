package com.tech.common.file.csv.exporter.service;

import com.tech.common.file.csv.dto.ProductDto;
import com.tech.common.file.csv.dto.UserDto;

import java.util.List;

/**
 * The interface Export csv.
 */
public interface ExportCsvService {

    /**
     * Export byte [ ].
     *
     * @param data the data
     * @return the byte [ ]
     */
    byte[] exportUser(List<UserDto> data);

    /**
     * Export product byte [ ].
     *
     * @param data the data
     * @return the byte [ ]
     */
    byte[] exportProduct(List<ProductDto> data);


}














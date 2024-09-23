package com.tech.common.controller;

import com.tech.common.aop.logging.SystemLogging;
import com.tech.common.file.csv.dto.UserDto;
import com.tech.common.file.csv.exporter.service.ExportCsvService;
import com.tech.common.http.HttpUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SystemLogging
@RequiredArgsConstructor
@RequestMapping("/export")
@RestController
public class ExporterController {

    private final ExportCsvService exportCsvService;

    @PostMapping
    public ResponseEntity<byte[]> export() {
        HttpHeaders headers = HttpUtil.buildContentHeader("text/csv; charset=UTF-8", "test.csv", false);
        return ResponseEntity.ok()
                .headers(headers)
                .body(exportCsvService.exportUser(UserDto.getUserDtos()));
    }
}

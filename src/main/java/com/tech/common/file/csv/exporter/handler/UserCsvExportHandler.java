package com.tech.common.file.csv.exporter.handler;

import com.tech.common.file.csv.dto.UserDto;
import com.tech.common.file.csv.exporter.BaseExportCsv;
import org.springframework.stereotype.Component;

@Component
public class UserCsvExportHandler extends BaseExportCsv<UserDto> {

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
        return "test.csv";
    }

    @Override
    protected String[] getHeader() {
        if (isNumberOrder()) {
            return new String[]{"STT", "Username", "Email", "Full Name"};
        } else {
            return new String[]{"Username", "Email", "Full Name"};
        }
    }

    @Override
    protected String[] convertToRow(UserDto item, int index) {
        if (isNumberOrder()) {
            return new String[]{String.valueOf(index), item.getUsername(), item.getEmail(), item.getFullName()};
        } else {
            return new String[]{item.getUsername(), item.getEmail(), item.getFullName()};
        }
    }
}

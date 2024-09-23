package com.tech.common.file.csv.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String username;
    private String email;
    private String fullName;

    public static List<UserDto> getUserDtos() {
        List<UserDto> userDtos = new ArrayList<>();
        userDtos.add(userDto());
        userDtos.add(userDto2());
        return userDtos;
    }

    public static UserDto userDto() {
        return new UserDto("anh", "anh@gmail.com", "hoang anh");
    }

    public static UserDto userDto2() {
        return new UserDto("a", "s", "a");
    }
}

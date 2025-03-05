package com.tech.common.controller;

import com.tech.common.aop.logging.SystemLogging;
import com.tech.common.entity.User;
import com.tech.common.repository.UserRepository;
import com.tech.common.response.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SystemLogging(isResponse = false)
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ResponseUtil<List<User>>> getUsers() {
        return ResponseEntity.ok(ResponseUtil.success(userRepository.findAll()));
    }
}

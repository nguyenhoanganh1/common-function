package com.tech.common.controller;

import com.tech.common.controller.request.UserRequest;
import com.tech.common.entity.User;
import com.tech.common.response.ResponseUtil;
import com.tech.common.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

//@SystemLogging(isResponse = false)
@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ResponseUtil<List<User>>> getUsers() {
        return ResponseEntity.ok(ResponseUtil.success(userService.getUsers()));
    }

    @PostMapping
    public ResponseEntity<ResponseUtil<User>> createUser(@RequestBody UserRequest request) {
        return ResponseEntity.ok(ResponseUtil.success(userService.createUser(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseUtil<User>> updateUser(@PathVariable("id") UUID id,
                                                         @RequestBody UserRequest request) {
        return ResponseEntity.ok(ResponseUtil.success(userService.updateUser(id, request)));
    }
}

package com.korit.authstudy.controller;

import com.korit.authstudy.dto.UserRegisterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/users")
public class UserController {

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterDto dto) {
        log.info("DTO: {}", dto);

        return ResponseEntity.ok("사용자 등록");
    }
}

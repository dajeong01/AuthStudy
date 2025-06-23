package com.korit.authstudy.controller;

import com.korit.authstudy.domain.entity.User;
import com.korit.authstudy.dto.*;
import com.korit.authstudy.dto.LoginDto;
import com.korit.authstudy.exception.MyAccountException;
import com.korit.authstudy.security.model.PrincipalUser;
import com.korit.authstudy.security.service.JwtService;
import com.korit.authstudy.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;
    private final JwtService jwtService;
    private LoginDto dto;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterDto dto) {
        log.info("DTO: {}", dto);
        return ResponseEntity.ok(usersService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        JwtDto jwtDto = usersService.login(dto);
        System.out.println("로그인 컨트롤러 호출");
        return ResponseEntity.ok(jwtDto);
    }

    @GetMapping("/login/status")
    public ResponseEntity<?> getLoginStatus(@RequestHeader("Authorization") String authorization) {
        System.out.println(authorization);
        return ResponseEntity.ok(jwtService.validLoginAccessToken(authorization));
    }

    @GetMapping("/principal")
    public ResponseEntity<?> getPrincipalUser() {
        return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication());
    }

    @PutMapping("/{usersId}")
    public ResponseEntity<?> modifyFullNameOrEmail(@PathVariable Integer usersId, @RequestBody UserModifyDto dto) {
        System.out.println(usersId);
        System.out.println(dto);
        usersService.modifyFullNameOrEmail(usersId, dto);
        return ResponseEntity.ok("변경 성공");
    }

    @PutMapping("/{usersId}/password")
    public ResponseEntity<?> modifyPassword(@PathVariable Integer usersId,
                                            @RequestBody UserPasswordModifyDto dto,
                                            @AuthenticationPrincipal PrincipalUser principalUser) { // @auth 이거 적으면 밑에 코드줄 자동으로 갖다줌
//        PrincipalUser principalUser = (PrincipalUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(usersId);
        System.out.println(dto);
        if(!usersId.equals(principalUser.getUserId())){
            throw new MyAccountException("본인의 계정만 변경할 수 있습니다.");
        }
        usersService.modifyPassword(dto, principalUser);
        return ResponseEntity.ok("변경 성공");
    }
}

package com.korit.authstudy.dto;

import com.korit.authstudy.domain.entity.Member;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
@RequiredArgsConstructor
public class MemberRegisterDto {

    private String username;
    private String password;
    private String fullName;
    private String email;

    public Member toEntity(BCryptPasswordEncoder passwordEncoder) {
        return Member.builder()
                .memberName(username)
                // vscode 에서 value변수를 usename으로 잡았기때문에 매개변수로 username 들고오기
                .password(passwordEncoder.encode(password))
                // 이거 위에 코드 설명 함만 더 ?
                .name(fullName)
                // vscode 에서 value변수를 fullName으로 잡았기때문에 매개변수로 fullName 들고오기
                .email(email)
                .build();
    }
}

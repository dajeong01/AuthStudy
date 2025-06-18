package com.korit.authstudy.service;

import com.korit.authstudy.domain.entity.User;
import com.korit.authstudy.dto.LoginDto;
import com.korit.authstudy.dto.LoginDto;
import com.korit.authstudy.dto.UserRegisterDto;
import com.korit.authstudy.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final BCryptPasswordEncoder passwordEncoder;

    private final UsersRepository usersRepository;

    public User register(UserRegisterDto dto) {
        User insertedUser = usersRepository.save(dto.toEntity(passwordEncoder));
        return insertedUser;
    }

    public void login(LoginDto dto) {
        List<User> foundUsers = usersRepository.findByUsername(dto.getUsername());
        if(foundUsers.isEmpty()){
            System.out.println("아이디 없음");
        }
        User user = foundUsers.get(0);
        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())){
            System.out.println("비밀번호 틀림");
        }
        System.out.println("로그인 성공 토큰 생성");

    }
}


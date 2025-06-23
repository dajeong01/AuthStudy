package com.korit.authstudy.service;

import com.korit.authstudy.domain.entity.User;
import com.korit.authstudy.dto.*;
import com.korit.authstudy.dto.LoginDto;
import com.korit.authstudy.mapper.UsersMapper;
import com.korit.authstudy.repository.UsersRepository;
import com.korit.authstudy.security.jwt.JwtUtil;
import com.korit.authstudy.security.model.PrincipalUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;
    private final JwtUtil jwtUtil;

    public User register(UserRegisterDto dto) {
        User insertedUser = usersRepository.save(dto.toEntity(passwordEncoder));
        return insertedUser;
    }

    public JwtDto login(LoginDto dto) {
        List<User> foundUsers = usersRepository.findByUsername(dto.getUsername());
        if(foundUsers.isEmpty()){
            throw new UsernameNotFoundException("사용자 정보(아이디를 못 찾음)를 확인하세요.");
            // error 401
            // 여기서 예외 터지면 바로 login 메서드 종료
        }
        User user = foundUsers.get(0);
        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())){
            throw new BadCredentialsException("사용자 정보(비밀번호 오류)를 확인하세요");
        }
        System.out.println("로그인 성공 토큰 생성");
        String token = jwtUtil.generateAccessToken(user.getId().toString());
        return JwtDto.builder().accessToken(token).build();
    }

    @Transactional(rollbackOn = Exception.class)
    public void modifyFullNameOrEmail(Integer usersId, UserModifyDto dto) {
        User user = dto.toEntity(usersId);
        int updateCount =usersRepository.updateFullNameOrEmailById(user);
//        int updateCount = usersMapper.updateFullNameOrEmailById(user);
        System.out.println(updateCount);
//        usersRepository.save();  // .save : id가 존재하면 update , 없다면 insert 기능 (문제점은 모든 column이 다 update됨)
    }

    @Transactional(rollbackOn = Exception.class)
    public void modifyPassword(UserPasswordModifyDto dto, PrincipalUser principalUser){
        // 1. 현재 로그인 되어있는 비밀번호와 요청때 받은 현재 비밀번호가 일치하는지 확인.
        if(!passwordEncoder.matches(dto.getOldPassword(), principalUser.getPassword())) {
            throw new BadCredentialsException("현재 비밀번호가 일치하지 않습니다.");
        }
        // 2. 새 비밀번호와 새 비밀번호 확인이 일치하는지.
        if(!dto.getNewPassword().equals(dto.getNewPasswordCheck())) {
            throw new BadCredentialsException("새 비밀번호가 일치하지 않습니다.");
        }
        String encodedPassword = passwordEncoder.encode(dto.getNewPassword());

        usersMapper.updatePassword(principalUser.getUserId(), encodedPassword);
    };
}


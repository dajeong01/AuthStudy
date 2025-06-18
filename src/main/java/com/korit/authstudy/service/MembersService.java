package com.korit.authstudy.service;

import com.korit.authstudy.domain.entity.Member;
import com.korit.authstudy.dto.MemberRegisterDto;
import com.korit.authstudy.repository.MembersRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MembersService {

    private final BCryptPasswordEncoder passwordEncoder;
    // 이거 한번만 더 설명 듣기

    private final MembersRepository membersRepository;
    // final 이 없으면 AllArgsConstructor / final이 붙으면 RequiredArgsConstructor로 만들어짐

    public Member register(MemberRegisterDto dto){
        Member insertedMember = membersRepository.save(dto.toEntity(passwordEncoder));
        return insertedMember;

    }
}

package com.korit.authstudy.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder                // builder 패턴에서는 @AllArgsConstructor가 필수로 동반되어야함
@Entity
@Table(name = "members_tb")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "members_id")
    private Integer id;

    private String memberName;
    private String password;
    private String name;
    private String email;
}

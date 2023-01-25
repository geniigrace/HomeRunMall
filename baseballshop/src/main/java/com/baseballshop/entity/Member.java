package com.baseballshop.entity;

import com.baseballshop.constant.Grade;
import com.baseballshop.constant.Role;
import com.baseballshop.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;


@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member extends BaseEntity{

    @Id
    @Column(name = "userNum")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userTeam;

    @Column(unique = true)
    private String userId;

    private String password;

    private String email;

    private String name;

    private String phone;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();

        member.setUserTeam(memberFormDto.getUserTeam());
        member.setUserId(memberFormDto.getUserId());
        String pw = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(pw);
        member.setEmail(memberFormDto.getEmail());
        member.setName(memberFormDto.getName());
        member.setPhone(memberFormDto.getPhone());
        member.setAddress(memberFormDto.getAddress());
        member.setRole(Role.ADMIN);
        member.setGrade(Grade.MASTER);

        return member;
    }

}

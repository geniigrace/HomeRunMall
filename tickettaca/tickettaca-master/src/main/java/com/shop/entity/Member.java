package com.shop.entity;

import com.shop.constant.Role;
import com.shop.dto.MemberFormDto;
import com.shop.dto.MemberHelpDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name="member")
@Getter @Setter
@ToString
public class Member extends BaseEntity {

    @Id
    @Column(name="userNum")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String userID;

    private String password;

    private String name;

    private String phone;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String address;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setUserID(memberFormDto.getUserID());
        member.setName(memberFormDto.getName());
        member.setPhone(memberFormDto.getPhone());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.USER);
        return member;
    }

//    public static Member searchMember(MemberHelpDto memberHelpDto){
//        Member member = new Member();
//        member.setName(memberHelpDto.getSearchName());
//        member.setPhone(member.getPhone());
//        return member;
//    }
}

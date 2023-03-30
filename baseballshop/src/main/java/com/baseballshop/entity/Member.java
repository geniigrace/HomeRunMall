package com.baseballshop.entity;

import com.baseballshop.constant.Role;
import com.baseballshop.constant.Team;
import com.baseballshop.dto.MemberFormDto;
import com.baseballshop.dto.MemberModifyDto;
import com.baseballshop.dto.SessionUser;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Member extends BaseTimeEntity{

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Team team;

//    @Column(unique = true)
//    private String userId;

    private String password;

    @Column(unique = true)
    private String email;

    private String name;

    private String picture;

    private String phone;

    private String address1;

    private String address2;

    @Enumerated(EnumType.STRING)
    private Role role;

//    @CreatedDate
//    @Column(updatable = false)
//    private String createTime;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();

        member.setTeam(memberFormDto.getTeam());
//        member.setUserId(memberFormDto.getUserId());
        String pw = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(pw);
        member.setEmail(memberFormDto.getEmail());
        member.setName(memberFormDto.getName());
        member.setPhone(memberFormDto.getPhone());
        member.setAddress1(memberFormDto.getAddress1());
        member.setAddress2(memberFormDto.getAddress2());
        member.setRole(Role.ADMIN);

        return member;
    }

//    @PrePersist
//    public void onPrePersist(){
//        this.createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm"));
//    }

    public Member (String name, String email, String picture){
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public Member update(String name, String picture){
        this.name = name;
        this.picture = picture;

        return this;
    }

    public void updateMember(MemberModifyDto memberModifyDto){
        if(memberModifyDto.getPassword() != null){
            this.password = memberModifyDto.getPassword();
        }
        this.team=memberModifyDto.getTeam();
        this.name=memberModifyDto.getName();
        this.phone= memberModifyDto.getPhone();
        this.address1= memberModifyDto.getAddress1();
        this.address2= memberModifyDto.getAddress2();
    }
}

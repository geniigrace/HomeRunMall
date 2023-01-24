package com.baseballshop.entity;

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
public class Member {

    @Id
    @Column(name = "member_key")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberKey;

    private String memberTeam;

    @Column(unique = true)
    private String memberId;

    private String memberPw;

    private String memberEmail;

    private String memberName;

    private String memberBirth;

    private String memberPhone;

    private String memberAddr;

    @Enumerated(EnumType.STRING)
    private Role memberRole;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        Member member = new Member();

        member.setMemberTeam(memberFormDto.getMemberTeam());
        member.setMemberId(memberFormDto.getMemberId());
        member.setMemberEmail(memberFormDto.getMemberEmail());
        member.setMemberName(memberFormDto.getMemberName());
        member.setMemberBirth(memberFormDto.getMemberBirth());
        member.setMemberPhone(memberFormDto.getMemberPhone());
        member.setMemberAddr(memberFormDto.getMemberAddr());
        member.setMemberRole(Role.ADMIN);

        String pw = passwordEncoder.encode(memberFormDto.getMemberPw());
        member.setMemberPw(pw);

        return member;
    }

}

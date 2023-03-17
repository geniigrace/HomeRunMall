package com.baseballshop.service;

import com.baseballshop.constant.Role;
import com.baseballshop.dto.SessionUser;
import com.baseballshop.entity.Member;
import com.baseballshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.security.Principal;

@Service
@Transactional
@RequiredArgsConstructor
public class LoginUserService {
    private final HttpSession httpSession;

    private final MemberRepository memberRepository;

    public String[] loginUserNameEmail(Principal principal){

            String[] loginUserInfo = new String[2];
            Member member = memberRepository.findByEmail(principal.getName());

            if(member==null){
                SessionUser user = (SessionUser) httpSession.getAttribute("member");

                loginUserInfo[0]=user.getName();
                loginUserInfo[1]=user.getEmail();

            }
            else {
                loginUserInfo[0]=member.getName();
                loginUserInfo[1]=member.getEmail();
            }
            return loginUserInfo;
    }

    public Member loginUserMember(Principal principal){
        Member member = memberRepository.findByEmail(principal.getName());

        if(member == null){
            SessionUser user = (SessionUser) httpSession.getAttribute("member");
            member = memberRepository.findByEmail(user.getEmail());
        }

        return member;
    }

    public boolean loginUserType(Principal principal){
        Member member = memberRepository.findByEmail(principal.getName());
        if (member == null) { //소셜로그인
            return true;
        } else { //로컬로그인
            return false;
        }
    }
}

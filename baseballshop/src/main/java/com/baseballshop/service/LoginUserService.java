package com.baseballshop.service;

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

    public boolean loginUserType(Principal principal){
        Member member = memberRepository.findByEmail(principal.getName());
        if (member == null) {
            return true;
        } else {
            return false;
        }
    }
}

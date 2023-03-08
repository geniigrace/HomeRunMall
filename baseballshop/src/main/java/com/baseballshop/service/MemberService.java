package com.baseballshop.service;

import com.baseballshop.config.OAuthAttributes;
import com.baseballshop.dto.MemberFormDto;
import com.baseballshop.dto.MemberModifyDto;
import com.baseballshop.dto.SessionUser;
import com.baseballshop.entity.Member;
import com.baseballshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final HttpSession httpSession;

    //입력받은 멤버데이터에서 ID 추출하여 중복확인 한 후 레포지로 넘김
    public Member saveMember(Member member){
        validateDuplicateMember(member);

        return memberRepository.save(member);
    }

    //입력받은 ID로 ID 중복 확인
    public void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());

        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(email);

        if(member==null){
            throw new UsernameNotFoundException(email);
        }

        //빌드패턴
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    //아이디 중복확인 버튼을 위한 중복확인 함수
    public Member validateCheck(String checkEmail){

        return  memberRepository.findByEmail(checkEmail);
    }

    public MemberModifyDto getMemberInfo(String email){
        Member member = memberRepository.findByEmail(email);

        MemberModifyDto memberModifyDto = new MemberModifyDto();
        memberModifyDto.setEmail(member.getEmail());
        memberModifyDto.setName(member.getName());
        memberModifyDto.setPhone(member.getPhone());
        memberModifyDto.setAddress(member.getAddress());
        memberModifyDto.setUserTeam(member.getUserTeam());

        return memberModifyDto;

    }

    public void updateMember(MemberModifyDto memberModifyDto, String email){
        Member member = memberRepository.findByEmail(email);
        member.updateMember(memberModifyDto);
    }
}

package com.baseballshop.service;

import com.baseballshop.entity.Member;
import com.baseballshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    //입력받은 멤버데이터에서 ID 추출하여 중복확인 한 후 레포지로 넘김
    public Member saveMember(Member member){
        validateDuplicateMember(member);

        return memberRepository.save(member);
    }

    //입력받은 ID로 ID 중복 확인
    public void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByMemberId(member.getMemberId());

        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }


    @Override
    public UserDetails loadUserByUsername(String memberID) throws UsernameNotFoundException {

        Member member = memberRepository.findByMemberId(memberID);

        if(member==null){
            throw new UsernameNotFoundException(memberID);
        }

        //빌드패턴
        return User.builder().username(memberID)
                .password(member.getMemberPw())
                .roles(member.getMemberRole().toString())
                .build();
    }

}

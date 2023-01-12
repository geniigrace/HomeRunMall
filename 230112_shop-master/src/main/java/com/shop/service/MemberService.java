package com.shop.service;

import com.shop.dto.MemberHelpDto;
import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByUserID(member.getUserID());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String userID) throws UsernameNotFoundException {

        Member member = memberRepository.findByUserID(userID);

        if(member == null){
            throw new UsernameNotFoundException(userID);
        }

        return User.builder()
                .username(member.getUserID())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    @Transactional(readOnly = true)
    public Member searchUserID(MemberHelpDto memberHelpDto){
        Member member1 = memberRepository.findByName(memberHelpDto.getSearchName());
        Member member2 = memberRepository.findByPhone(memberHelpDto.getSearchPhone());
        if(member1 != null && member2 != null && member1.getUserID().equals(member2.getUserID())){
            return member1;
        }
        else{
            return null;
        }
    }

}
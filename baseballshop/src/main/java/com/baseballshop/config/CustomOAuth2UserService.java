package com.baseballshop.config;

import com.baseballshop.dto.SessionUser;
import com.baseballshop.entity.Member;
import com.baseballshop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;


@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);

        // 현재 진행중인 서비스를 구부하기 위해 문자열로 받음. oAuth2UserRequest.getClinetRegistration().getREgistrationId()
        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();

        //OAuth2 로그인 시 키 값이 도니다. 구글은 키 값이 "sub" dlrh, 네이버는 "response"이고, 카카오는 "id" 이다.
        String userNameAttributeName = oAuth2UserRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        //oAuth2 로그인을 통해 가져온 OAuth2USer의 attribute를 담아주는  of 메소드
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Member member = saveOrUpdate(attributes);
        httpSession.setAttribute("member", new SessionUser(member));

        System.out.println(attributes.getAttributes());
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ADMIN"))
                , attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }
    //혹시 이미 저장된 정보라면, update 처리
    private Member saveOrUpdate(OAuthAttributes attributes){
//        Member member = memberRepository.findByEmail(attributes.getEmail()).map(entity -> entity.update(attributes.getName(), attributes.getEmail())).orElse(attributes.toEntity());
         Member checkMember = memberRepository.findByEmail(attributes.getEmail());

        Member member;

         if(checkMember==null){
              member = new Member();
              member.setEmail(attributes.getEmail());
              member.setPicture(attributes.getPicture());
              member.setName(attributes.getName());
              member.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm")));
         }
         else {
            member = checkMember.update(attributes.getName(), attributes.getPicture());
         }

        return memberRepository.save(member);
    }


}

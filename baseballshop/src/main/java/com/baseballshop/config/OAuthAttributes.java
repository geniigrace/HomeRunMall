package com.baseballshop.config;

import com.baseballshop.entity.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
public class OAuthAttributes {

    private Map<String, Object> attributes;

    private String nameAttributeKey;

    private String name;

    private String email;

    private String picture;

    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture){
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    //생성자 오버로딩
    public OAuthAttributes(){

    }

    //해당 로그인인 서비스가 kakao 인지 googole인지 구분하여, 알맞게 매핑을 해주도록 하빈다.
    //여기에서 registraitonId는 Oauth2 로그인을 처리한 서비스 명 이 되고, userNameAttributeName은 해당 서비스의 map의 키값이 되는 값입니다.
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
        if(registrationId.equals("kakao")){
            return ofKaKao(userNameAttributeName, attributes);
        }else if( registrationId.equals("naver")){
            return ofNaver(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKaKao(String userNameAttributeName, Map<String, Object> attributes){
        Map<String, Object> kakao_account = (Map<String, Object>)attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>)kakao_account.get("profile");

        return new OAuthAttributes(attributes, userNameAttributeName,
                (String)profile.get("nickname"),
                (String)kakao_account.get("email"),
                (String)profile.get("profile_image_url"));
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes){
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        return new OAuthAttributes(attributes,userNameAttributeName,
                (String) response.get("name"),
                (String) response.get("email"),
                (String) response.get("profile_image"));
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes){
        return new OAuthAttributes(attributes, userNameAttributeName,
                (String) attributes.get("name"),
                (String) attributes.get("email"),
                (String) attributes.get("picture"));
    }
    //getter setter 생략

    public Member toEntity() {
        return new Member(name, email, picture);
    }
}

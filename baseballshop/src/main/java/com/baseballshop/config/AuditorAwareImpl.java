package com.baseballshop.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    //Optional 클래스는 아래와 같은 value 에 값을 저장하기 때문에 null 이라도 바로 nullpointexception이 발생하지 않음
    // 클래스이기 때문에 각종 메서드를 제공함

    @Override
    public Optional<String> getCurrentAuditor(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = "";
        if(authentication != null){
            userId = authentication.getName();
        }

        return Optional.of(userId);
    }
}

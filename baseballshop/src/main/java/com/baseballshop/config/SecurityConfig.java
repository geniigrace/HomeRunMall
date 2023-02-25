package com.baseballshop.config;

import com.baseballshop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.formLogin()
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/")
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .oauth2Login()
                .defaultSuccessUrl("/")
                .userInfoEndpoint()
                .userService(customOAuth2UserService);


        http.authorizeRequests()

                .mvcMatchers("/","/members/**","/item/**","/images/**","/**","/notice/**","/community/**").permitAll() //해당경로는 모든 경우
                .mvcMatchers("/admin/**").hasRole("ADMIN") //admin 경로는 권한이 admin인 경우
                .mvcMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated();

        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); //비밀번호를 암호화 하는 해시함수
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }

//    @Override
//    public void configure(WebSecurity web) throws Exception{
//
//        web.ignoring().antMatchers("/css/**","/js/**","/img/**"); //이 경로에 있는 것들은 시큐리티에서 제외
//    }

}

package com.baseballshop.controller;

import com.baseballshop.config.CustomOAuth2UserService;
import com.baseballshop.entity.Member;
import com.baseballshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping("/user")
@Controller
@RequiredArgsConstructor
public class UserController {

    private final MemberRepository memberRepository;

    private final CustomOAuth2UserService customOAuth2UserService;

    //마이페이지
    @GetMapping(value = "/mypage")
    public String mypage(Model model, Principal principal){

        if(principal!=null) {
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                model.addAttribute("loginName",customOAuth2UserService.loadLoginUserName());
            } else {
                model.addAttribute("loginName", member.getName());
            }
        }

        return "user/mypage";
    }
    //장바구니
    @GetMapping(value = "/cart")
    public String cart(Model model, Principal principal){

        if(principal!=null) {
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                model.addAttribute("loginName",customOAuth2UserService.loadLoginUserName());
            } else {
                model.addAttribute("loginName", member.getName());
            }
        }

        return "user/cart";
    }

    //찜목록
    @GetMapping(value = "/like")
    public String like(Model model, Principal principal){

        if(principal!=null) {
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                model.addAttribute("loginName",customOAuth2UserService.loadLoginUserName());
            } else {
                model.addAttribute("loginName", member.getName());
            }
        }

        return "user/like";
    }

    //주문내역
    @GetMapping(value = "/orderlist")
    public String orderlist(Model model, Principal principal){

        if(principal!=null) {
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                model.addAttribute("loginName",customOAuth2UserService.loadLoginUserName());
            } else {
                model.addAttribute("loginName", member.getName());
            }
        }

        return "user/orderlist";
    }

    //주문서 작성
    @GetMapping(value = "/order")
    public String order(Model model, Principal principal){

        if(principal!=null) {
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                model.addAttribute("loginName",customOAuth2UserService.loadLoginUserName());
            } else {
                model.addAttribute("loginName", member.getName());
            }
        }

        return "user/order";
    }

    //회원정보 조회
    @GetMapping(value = "/myinfo")
    public String myinfo(Model model, Principal principal){

        if(principal!=null) {
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                model.addAttribute("loginName",customOAuth2UserService.loadLoginUserName());
            } else {
                model.addAttribute("loginName", member.getName());
            }
        }

        return "user/myinfo";
    }

    //회원정보 수정 : 회원가입 페이지에서 읽어오고 아이디만 수정할수 없게 처리
    @GetMapping(value = "/myinfoEdit")
    public String myinfoEdit(Model model, Principal principal){

        if(principal!=null) {
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                model.addAttribute("loginName",customOAuth2UserService.loadLoginUserName());
            } else {
                model.addAttribute("loginName", member.getName());
            }
        }

        return "user/myinfoEdit";
    }
}

package com.shop.controller;

import com.shop.dto.MemberFormDto;
//import com.shop.dto.MemberHelpDto;
import com.shop.dto.MemberHelpDto;
import com.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.entity.Member;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.validation.BindingResult;
import javax.validation.Valid;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping(value = "/new")
    public String memberForm(Model model){
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }

        return "redirect:/";
    }

    @GetMapping(value = "/login")
    public String loginMember(){
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/help")
    public String helpMember(Model model){
        model.addAttribute("userID", null);
        return "member/help";
    }

//    @PostMapping(value = "/help")
//    public String sendHelpResult(MemberHelpDto memberHelpDto, Model model){
//        Member member = memberService.searchUserID(memberHelpDto);
//        if(member != null){
//            model.addAttribute("userID", member.getUserID());
//        }
//        else{
//            model.addAttribute("userID", null);
//        }
//        return "/member/help";
//    }

//    @GetMapping(value = "/helpResult")
//    public String helpResultPage(){
//        return "/member/helpResult";
//    }

    @PostMapping(value = "/help")
    public String helpResult(MemberHelpDto memberHelpDto, Model model){

        Member member = memberService.searchUserID(memberHelpDto);

        if(member != null){
            model.addAttribute("userID", member.getUserID());
        }
        else{
            model.addAttribute("userID", "None");
        }
        return "member/help";

//        try{
//            Member member = Member.searchMember(memberHelpDto);
//        }
    }

}
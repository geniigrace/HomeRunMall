package com.baseballshop.controller;

import com.baseballshop.dto.MemberFormDto;
import com.baseballshop.entity.Member;
import com.baseballshop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    //회원가입 페이지를 위한 맵핑
    @GetMapping(value = "/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping(value = "/new")
    public String memberForm(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            return "member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);

            response.setContentType("text/html; charset=euc-kr");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('회원가입이 완료되었습니다!'); location.href='/members/login'; </script>");
            out.flush();

            return "member/memberLoginForm";

        } catch (IllegalStateException | IOException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "member/memberForm";
        }
    }
    //로그인 페이지를 위한 맵핑
    @GetMapping(value = "/login")
    public String loginMember() {
        return "/member/memberLoginForm";
    }

    @GetMapping(value = "/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요.");
        return "/member/memberLoginForm";
    }

    //이메일 중복확인을 위한 맵핑
    @PostMapping(value = "/validatecheck")
    public @ResponseStatus ResponseEntity validateCheck(@RequestBody @Valid Map<String, String> email, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for (FieldError fieldError : fieldErrors) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        Member checked;
        boolean result;
        String checkEmail = email.get("email");

        if(checkEmail==null||checkEmail==""){
            return new ResponseEntity<String>("이메일을 입력하세요.", HttpStatus.BAD_REQUEST);
        }
        else {
            try {
                checked = memberService.validateCheck(checkEmail);

                if (checked == null) {

                    result = false;

                } else {

                    result = true;

                }
            } catch (Exception e) {
                return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<Boolean>(result, HttpStatus.OK);
        }
    }

}

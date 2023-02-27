package com.baseballshop.controller;

import com.baseballshop.config.CustomOAuth2UserService;
import com.baseballshop.dto.CartDto;
import com.baseballshop.dto.CartItemDto;
import com.baseballshop.dto.SessionUser;
import com.baseballshop.entity.Member;
import com.baseballshop.repository.MemberRepository;
import com.baseballshop.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RequestMapping("/user")
@Controller
@RequiredArgsConstructor
public class UserController {

    private final CartService cartService;
    private final MemberRepository memberRepository;
    private final HttpSession httpSession;
    //마이페이지
    @GetMapping(value = "/mypage")
    public String mypage(Model model, Principal principal){

        if(principal!=null){
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                SessionUser user = (SessionUser)httpSession.getAttribute("member");
                model.addAttribute("loginName", user.getName());

            } else {
                model.addAttribute("loginName", member.getName());
            }
        }

        return "user/mypage";
    }
    //장바구니
    @GetMapping(value = "/cart")
    public String orderHist(Principal principal, Model model){

        List<CartDto> cartDtoList;
        String email="";

        if(principal!=null){ //로그인 했을 때
            Member member = memberRepository.findByEmail(principal.getName());

            if (member == null) {//소셜 로그인 일 때
                SessionUser user = (SessionUser)httpSession.getAttribute("member");

                //사용자 이름
                model.addAttribute("loginName", user.getName());

                //카트로 연결
                email = user.getEmail();
                cartDtoList = cartService.getCartList(email);

            } else {//로컬 로그인 일 때
                //사용자 이름
                model.addAttribute("loginName", member.getName());

                //카트로 연결
                email= principal.getName();
                cartDtoList = cartService.getCartList(email);
            }
            model.addAttribute("cartItems", cartDtoList);
        }
        return "/user/cart";
    }

    @PostMapping(value = "/cart")
    public @ResponseBody ResponseEntity addCart (@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email="";
        Long cartItemId;

        try{
            if(principal!=null) { //로그인 했을 때
                Member member = memberRepository.findByEmail(principal.getName());

                if (member == null) {//소셜 로그인 일 때
                    SessionUser user = (SessionUser)httpSession.getAttribute("member");
                    email = user.getEmail();
                    cartItemId = cartService.addCart(cartItemDto, email);

                } else {//로컬 로그인 일 때
                    email = principal.getName();
                    cartItemId = cartService.addCart(cartItemDto, email);
                }
            }
            else {
                return new ResponseEntity<String>("로그인이 필요합니다.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @GetMapping(value = "/like")
    public String like(Model model, Principal principal){

        if(principal!=null){
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                SessionUser user = (SessionUser)httpSession.getAttribute("member");
                model.addAttribute("loginName", user.getName());

            } else {
                model.addAttribute("loginName", member.getName());
            }
        }

        return "user/like";
    }

    //주문내역
    @GetMapping(value = "/orderlist")
    public String orderlist(Model model, Principal principal){

        if(principal!=null){
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                SessionUser user = (SessionUser)httpSession.getAttribute("member");
                model.addAttribute("loginName", user.getName());

            } else {
                model.addAttribute("loginName", member.getName());
            }
        }

        return "user/orderlist";
    }

    //주문서 작성
    @GetMapping(value = "/order")
    public String order(Model model, Principal principal){

        if(principal!=null){
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                SessionUser user = (SessionUser)httpSession.getAttribute("member");
                model.addAttribute("loginName", user.getName());

            } else {
                model.addAttribute("loginName", member.getName());
            }
        }

        return "user/order";
    }

    //회원정보 조회
    @GetMapping(value = "/myinfo")
    public String myinfo(Model model, Principal principal){

        if(principal!=null){
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                SessionUser user = (SessionUser)httpSession.getAttribute("member");
                model.addAttribute("loginName", user.getName());

            } else {
                model.addAttribute("loginName", member.getName());
            }
        }

        return "user/myinfo";
    }

    //회원정보 수정 : 회원가입 페이지에서 읽어오고 아이디만 수정할수 없게 처리
    @GetMapping(value = "/myinfoEdit")
    public String myinfoEdit(Model model, Principal principal){

        if(principal!=null){
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                SessionUser user = (SessionUser)httpSession.getAttribute("member");
                model.addAttribute("loginName", user.getName());

            } else {
                model.addAttribute("loginName", member.getName());
            }
        }

        return "user/myinfoEdit";
    }
}

package com.baseballshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/user")
@Controller
@RequiredArgsConstructor
public class UserController {

    //마이페이지
    @GetMapping(value = "/mypage")
    public String mypage(){
        return "user/mypage";
    }
    //장바구니
    @GetMapping(value = "/cart")
    public String cart(){
        return "user/cart";
    }

    //찜목록
    @GetMapping(value = "/like")
    public String like(){
        return "user/like";
    }

    //주문내역
    @GetMapping(value = "/orderlist")
    public String orderlist(){
        return "user/orderlist";
    }

    //주문서 작성
    @GetMapping(value = "/order")
    public String order(){
        return "user/order";
    }

    //회원정보 조회
    @GetMapping(value = "/myinfo")
    public String myinfo(){
        return "user/myinfo";
    }

    //회원정보 수정 : 회원가입 페이지에서 읽어오고 아이디만 수정할수 없게 처리
    @GetMapping(value = "/myinfoEdit")
    public String myinfoEdit(){
        return "user/myinfoEdit";
    }
}

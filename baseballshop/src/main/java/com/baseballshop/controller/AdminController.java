package com.baseballshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {

    //관리페이지
    @GetMapping(value = "/adminpage")
    public String adminpage(){
        return "admin/adminpage";
    }

    //상품등록
    @GetMapping(value ="/item/new")
    public String itemNew(){
        return "admin/itemForm";
    }

    //상품관리
    @GetMapping(value = "/items")
    public String itemList(){
        return "admin/items";
    }

    //전체주문내역
    @GetMapping(value = "/orders")
    public String allOders(){
        return "admin/orders";
    }

    //공지등록
    @GetMapping(value = "/notice/new")
    public String noticeForm(){
        return "admin/noticeForm";
    }

    //회원관리
    @GetMapping(value = "/members" )
    public String memberList(){
        return "admin/members";
    }
}

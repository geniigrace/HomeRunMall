package com.baseballshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
public class CommunityController {

    //커뮤니티
    @GetMapping(value = "/community")
    public String community(){
        return "community/community";
    }

    //공지사항
    @GetMapping(value = "/notice")
    public String notice(){
        return "community/notice";
    }

}

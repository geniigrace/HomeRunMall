package com.cyoworld.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class profileController {

    @GetMapping(value= "/profile")
    public String profile(){

        return "profile/profileDtl";
    }

}

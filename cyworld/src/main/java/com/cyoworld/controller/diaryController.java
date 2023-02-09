package com.cyoworld.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class diaryController {

    @GetMapping(value= "/diary")
    public String diary(){

        return "diary/diaryDtl";
    }
}

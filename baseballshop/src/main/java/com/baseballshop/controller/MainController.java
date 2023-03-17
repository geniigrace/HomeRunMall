package com.baseballshop.controller;

import com.baseballshop.dto.ItemSearchDto;
import com.baseballshop.dto.MainItemDto;
import com.baseballshop.service.ItemService;
import com.baseballshop.service.LoginUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final ItemService itemService;
    private final LoginUserService loginUserService;

    @GetMapping(value = "/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model, Principal principal) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        if (itemSearchDto.getSearchQuery() == null) {
            itemSearchDto.setSearchQuery("");
        }

        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        if(principal!=null){ //로그인 했을 때
            String loginName = loginUserService.loginUserNameEmail(principal)[0];
            model.addAttribute("loginName", loginName);
        }

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 10);
        return "main";
    }
}
package com.baseballshop.controller;

import com.baseballshop.config.CustomOAuth2UserService;
import com.baseballshop.config.OAuthAttributes;
import com.baseballshop.dto.ItemSearchDto;
import com.baseballshop.dto.MainItemDto;
import com.baseballshop.dto.SessionUser;
import com.baseballshop.entity.Member;
import com.baseballshop.repository.MemberRepository;
import com.baseballshop.service.ItemService;
import com.baseballshop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final ItemService itemService;

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    private final HttpSession httpSession;

    @GetMapping(value = "/")
    public String main(ItemSearchDto itemSearchDto, Optional<Integer> page, Model model, Principal principal) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        if (itemSearchDto.getSearchQuery() == null) {
            itemSearchDto.setSearchQuery("");
        }

        Page<MainItemDto> items = itemService.getMainItemPage(itemSearchDto, pageable);

        if(principal!=null){ //로그인 했을 때
            Member member = memberRepository.findByEmail(principal.getName());

            if (member == null) { //소셜로그인 일 때
                SessionUser user = (SessionUser)httpSession.getAttribute("member");
                model.addAttribute("loginName", user.getName());

            } else { //로컬 로그인 일 때
                model.addAttribute("loginName", member.getName());
            }
        }

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 10);
        return "main";
    }
}
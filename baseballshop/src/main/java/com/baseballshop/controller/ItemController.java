package com.baseballshop.controller;

import com.baseballshop.constant.ItemCategory;
import com.baseballshop.dto.ItemFormDto;
import com.baseballshop.dto.ItemListDto;
import com.baseballshop.dto.ItemSearchDto;
import com.baseballshop.dto.QnaListDto;
import com.baseballshop.service.ItemService;
import com.baseballshop.service.LoginUserService;
import com.baseballshop.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final LoginUserService loginUserService;
    private final QnaService qnaService;

    @GetMapping(value = "/itemSearch/{itemCategory}")
    public String itemList(@PathVariable("itemCategory")String itemCategory, Optional<Integer> page, Model model, Principal principal, ItemSearchDto itemSearchDto){

        if(principal!=null){
            String loginName = loginUserService.loginUserNameEmail(principal)[0];
            model.addAttribute("loginName", loginName);
        }

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        if (itemSearchDto.getSearchQuery() == null) {
            itemSearchDto.setSearchQuery("");
        }

        String itemCategoryTitle = ItemCategory.valueOf(itemCategory).getTitle();
        String itemCategoryKey = ItemCategory.valueOf(itemCategory).getKey();

        Page<ItemListDto> items = itemService.getItemListPage(itemSearchDto, itemCategory, pageable);

        model.addAttribute("itemCategoryKey", itemCategoryKey);
        model.addAttribute("itemCategoryTitle",itemCategoryTitle);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 10);

        return "item/itemList";
    }

    @GetMapping(value = "/itemSearch/{itemCategory}/{team}")
    public String itemList(@PathVariable("itemCategory")String itemCategory, @PathVariable("team")String team, Optional<Integer> page, Model model, Principal principal, ItemSearchDto itemSearchDto){

        if(principal!=null){
            String loginName = loginUserService.loginUserNameEmail(principal)[0];
            model.addAttribute("loginName", loginName);
        }

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        if (itemSearchDto.getSearchQuery() == null || itemSearchDto.getSearchQuery().equals("undefined")) {
            itemSearchDto.setSearchQuery("");
        }

        String itemCategoryTitle = ItemCategory.valueOf(itemCategory).getTitle();
        String itemCategoryKey = ItemCategory.valueOf(itemCategory).getKey();

        Page<ItemListDto> items = itemService.getTeamItemListPage(itemSearchDto, itemCategory, team,  pageable);

        model.addAttribute("itemCategoryKey", itemCategoryKey);
        model.addAttribute("itemCategoryTitle",itemCategoryTitle);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 10);

        return "item/itemList";
    }

    @GetMapping(value = {"/item/{itemId}","/item/{itemId}/{page}"})
    public String itemDtl(@PathVariable("page") Optional<Integer> page, Model model, Principal principal, @PathVariable("itemId")Long itemId){

        if(principal!=null){
            String loginName = loginUserService.loginUserNameEmail(principal)[0];
            model.addAttribute("loginName", loginName);
        }

        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        Page<QnaListDto> qna = qnaService.getQnaPage(pageable);

        model.addAttribute("qna", qna);
        model.addAttribute("maxPage", 10);

        return "item/itemDtl";
    }
}

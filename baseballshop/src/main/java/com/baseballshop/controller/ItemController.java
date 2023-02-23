package com.baseballshop.controller;

import com.baseballshop.constant.ItemCategory;
import com.baseballshop.dto.ItemListDto;
import com.baseballshop.dto.ItemSearchDto;
import com.baseballshop.dto.MainItemDto;
import com.baseballshop.entity.Item;
import com.baseballshop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/item/{itemCategory}")
    public String itemList(@PathVariable("itemCategory")String itemCategory, Optional<Integer> page, Model model, ItemSearchDto itemSearchDto){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);


        String itemCategoryTitle = ItemCategory.valueOf(itemCategory).getTitle();
        String itemCategoryKey = ItemCategory.valueOf(itemCategory).getKey();

        Page<ItemListDto> items = itemService.getItemListPage(itemCategory, pageable);



        model.addAttribute("itemCategoryKey", itemCategoryKey);
        model.addAttribute("itemCategoryTitle",itemCategoryTitle);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 10);

        return "itemList/itemList";
    }

    @GetMapping(value = "/item/{itemCategory}/{team}")
    public String itemList(@PathVariable("itemCategory")String itemCategory, @PathVariable("team")String team, Optional<Integer> page, Model model, ItemSearchDto itemSearchDto){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);


        String itemCategoryTitle = ItemCategory.valueOf(itemCategory).getTitle();
        String itemCategoryKey = ItemCategory.valueOf(itemCategory).getKey();

        Page<ItemListDto> items = itemService.getTeamItemListPage(itemCategory, team,  pageable);



        model.addAttribute("itemCategoryKey", itemCategoryKey);
        model.addAttribute("itemCategoryTitle",itemCategoryTitle);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 10);

        return "itemList/itemList";
    }
}

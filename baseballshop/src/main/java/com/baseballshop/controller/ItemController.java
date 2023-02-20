package com.baseballshop.controller;

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


        Page<ItemListDto> items = itemService.getItemListPage(itemCategory, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 10);

        return "itemList/itemList";
    }
}

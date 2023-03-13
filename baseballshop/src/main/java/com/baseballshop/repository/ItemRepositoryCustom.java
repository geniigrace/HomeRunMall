package com.baseballshop.repository;

import com.baseballshop.dto.ItemListDto;
import com.baseballshop.dto.ItemSearchDto;
import com.baseballshop.dto.MainItemDto;
import com.baseballshop.entity.Item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    //상품관리페이지
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);


    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<ItemListDto> getItemListPage(ItemSearchDto itemSearchDto, String itemCategory, Pageable pageable);

    Page<ItemListDto> getTeamItemListPage(ItemSearchDto itemSearchDto, String itemCategory,String tea, Pageable pageable);
}

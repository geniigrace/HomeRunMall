package com.baseballshop.dto;

import com.baseballshop.constant.ItemCategory;
import com.baseballshop.constant.SellStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemListDto {

    private Long id;
    private String itemName;

    private String imgUrl;

    private Integer price;

    private ItemCategory category;

    private SellStatus sellStatus;

    @QueryProjection // QureyDSL 조회시 ItemListDto 객체로 바로 오도록 활용 : DTO가 QDTO로 나올 수 있음
    public ItemListDto(Long id, String itemName, String imgUrl, Integer price, ItemCategory category, SellStatus sellStatus){

        this.id=id;
        this.itemName = itemName;
        this.imgUrl = imgUrl;
        this.price = price;
        this.category = category;
        this.sellStatus=sellStatus;

    }
}

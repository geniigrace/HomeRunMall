package com.baseballshop.dto;

import com.baseballshop.constant.ItemCategory;
import com.baseballshop.constant.SellStatus;
import com.baseballshop.constant.Team;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemsDto {

    private Long id;
    private SellStatus sellStatus;

    private Team team;

    private ItemCategory category;

    private String imgUrl;
    private String itemName;

    private Integer price;

    private Integer stockNumber;

    @QueryProjection // QureyDSL 조회시 MainItemDto 객체로 바로 오도록 활용 : DTO가 QDTO로 나올 수 있음
    public ItemsDto(Long id, SellStatus sellStatus, Team team, ItemCategory itemCategory, String imgUrl, String itemName, Integer price, Integer stockNumber){

        this.id=id;
        this.sellStatus=sellStatus;
        this.team=team;
        this.category=itemCategory;
        this.imgUrl=imgUrl;
        this.itemName=itemName;
        this.price=price;
        this.stockNumber=stockNumber;

    }

}

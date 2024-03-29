package com.baseballshop.dto;

import com.baseballshop.constant.SellStatus;
import com.baseballshop.constant.Team;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {

    private Long id;
    private String itemName;

    private String itemDetail;

    private String imgUrl;

    private Integer price;

    private SellStatus sellStatus;

    @QueryProjection // QureyDSL 조회시 MainItemDto 객체로 바로 오도록 활용 : DTO가 QDTO로 나올 수 있음
    public MainItemDto(Long id, String itemName, String itemDetail, String imgUrl, Integer price, SellStatus sellStatus){

        this.id=id;
        this.itemName = itemName;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
        this.sellStatus=sellStatus;
    }

}

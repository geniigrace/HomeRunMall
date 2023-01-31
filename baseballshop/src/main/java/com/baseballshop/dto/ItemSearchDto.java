package com.baseballshop.dto;

import com.baseballshop.constant.SellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {

    //판매상태
    private SellStatus searchSellStatus;

    //구단
    private String searchTeam;

    //상품종류
    private String searchCategory;

    //등록일
    private String searchDateType;

    //상품명
    private String searchBy;

    private String searchQuery="";


}

package com.baseballshop.dto;

import com.baseballshop.constant.ItemCategory;
import com.baseballshop.constant.ShowStatus;
import com.baseballshop.constant.SellStatus;
import com.baseballshop.constant.Team;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {

    //노출여부
    private ShowStatus showStatus;

    //판매상태
    private SellStatus searchSellStatus;

    //구단
    private Team searchTeam;

    //상품종류
    private ItemCategory searchCategory;

    //등록일
    private String searchDateType;

    //상품명
    private String searchBy;

    private String searchQuery="";


}

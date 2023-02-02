package com.baseballshop.dto;

import com.baseballshop.constant.NoticeStatus;
import com.baseballshop.constant.ShowStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeSearchDto {

    private ShowStatus showStatus;

    private NoticeStatus searchNoticeStatus;

    private String searchDateType;

    //private ItemSellStatus searchSellStatus;

    private String searchBy;

    private String searchQuery = "";

}

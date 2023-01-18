package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import com.shop.constant.NoticeStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
public class NoticeSearchDto {

    private NoticeStatus searchNoticeStatus;

    private String searchDateType;

    //private ItemSellStatus searchSellStatus;

    private String searchBy;

    private String searchQuery = "";

}

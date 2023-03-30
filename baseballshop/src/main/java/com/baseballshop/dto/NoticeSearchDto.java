package com.baseballshop.dto;

import com.baseballshop.constant.ItemCategory;
import com.baseballshop.constant.NoticeStatus;
import com.baseballshop.constant.ShowStatus;
import com.baseballshop.constant.Team;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeSearchDto {

    private ShowStatus showStatus;

    private Team team;

    private ItemCategory category;

    private NoticeStatus searchNoticeType;

    private String searchDateType;

    private String searchBy;

    private String searchQuery = "";

}

package com.shop.dto;

import com.shop.constant.NoticeStatus;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NoticeDto {

    private Long id;

    private String noticeTitle;

    private String noticeContent;

    private NoticeStatus noticeStatus;

    private String noticeDate;


}

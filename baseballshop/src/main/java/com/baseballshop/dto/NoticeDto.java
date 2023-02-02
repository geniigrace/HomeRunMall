package com.baseballshop.dto;

import com.baseballshop.constant.NoticeStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NoticeDto {

    private Long id;

    private String noticeTitle;

    private String noticeContent;

    private NoticeStatus noticeStatus;

    private String noticeDate;

    private List<NoticeDto> noticeDtoList;


}
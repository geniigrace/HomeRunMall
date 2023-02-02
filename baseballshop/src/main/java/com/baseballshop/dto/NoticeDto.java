package com.baseballshop.dto;

import com.baseballshop.constant.NoticeStatus;
import com.baseballshop.constant.ShowStatus;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class NoticeDto {

    private Long id;

    private ShowStatus showStatus;


    private String noticeTitle;


    private String noticeContent;


    private NoticeStatus noticeStatus;

    private String noticeDate;

    private List<NoticeDto> noticeDtoList;


}
package com.baseballshop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NoticeDeleteDto {

    private Long noticeId;

    private List<NoticeDeleteDto> noticeDeleteDtoList;
}

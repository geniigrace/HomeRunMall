package com.shop.dto;

import com.shop.constant.NoticeStatus;
import com.shop.constant.Role;
import com.shop.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.security.PrivateKey;

@Getter
@Setter
public class NoticeDto {

    private Long id;

    private String noticeTitle;

    private String noticeContent;

    private NoticeStatus noticeStatus;

    private String noticeDate;


}

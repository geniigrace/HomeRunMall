package com.baseballshop.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NoticeStatus {
    NOTICE("NOTICE", "공지사항"),
    EVENT("EVENT","이벤트");

    private final String key;
    private final String title;
}

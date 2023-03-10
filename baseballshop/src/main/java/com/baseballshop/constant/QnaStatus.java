package com.baseballshop.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QnaStatus {
    ITEM("ITEM","상품문의"),
    DELIVERY("DELIVERY", "배송문의"),
    ETC("ETC","기타문의"),

    NOT("NOT","확인중"),
    DONE("DONE","답변완료");

    private final String key;
    private final String title;

}

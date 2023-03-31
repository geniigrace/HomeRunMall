package com.baseballshop.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QnaStatus {
    ITEM("ITEM","상품 문의"),
    DELIVERY("DELIVERY", "배송 문의"),
    CANCEL("CANCEL", "교환및취소 문의"),
    ETC("ETC","기타 문의"),

    FAQ("FAQ","FAQ"),
    NOT("NOT","확인중"),
    DONE("DONE","답변완료");

    private final String key;
    private final String title;

}

package com.baseballshop.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SellStatus {

    SELL("SELL","판매중"),
    SOLD_OUT("SOLD_OUT","품절");

    private final String key;
    private final String title;

}

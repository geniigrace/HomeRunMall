package com.baseballshop.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemCategory {

    UNIFORM("UNIFORM","유니폼"),
    BALLCAP("BALLCAP","모자"),
    CLOTHES("CLOTHES","의류"),
    GOODS("GOODS","잡화"),
    BBITEM("BBITEM","야구용품"),
    CHEERITEM("CHEERITEM","응원용품"),
    PLAYER("PLAYER","플레이어"),
    EVENT("EVENT","기획전");

    private final String key;
    private final String title;
}

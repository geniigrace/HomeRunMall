package com.baseballshop.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Team {

    KBO("KBO","KBO"),
    KOREA("TEAM_KOREA", "대표팀"),
    NC("NC","NC다이노스"),
    SS("SS","삼성라이온즈"),
    SSG("SSG","SSG랜더스"),
    DS("DS","두산베어스"),
    HH("HH","한화이글스"),
    KT("KT","KT위즈"),
    LT("LT","롯데자이언츠"),
    KIA("KIA","기아타이거즈"),
    LG("LG","LG트윈스"),
    KW("KW","키움히어로즈"),
    ETC("ETC","기타");

    private final String key;
    private final String title;

}

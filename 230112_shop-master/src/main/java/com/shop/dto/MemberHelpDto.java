package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class MemberHelpDto {

    private String searchName;

    private String searchPhone;

//    private String searchQuery = "";
}

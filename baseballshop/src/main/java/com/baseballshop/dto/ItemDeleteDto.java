package com.baseballshop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ItemDeleteDto {

    private Long itemId;

    private List<ItemDeleteDto> itemDeleteDtoList;
}

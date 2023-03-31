package com.baseballshop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDto {

    private Long cartItemId;

    private String itemName;

    private int price;

    private int count;

    private String imgUrl;

    private Long itemId;

    public CartDto(Long cartItemId, String itemName, int price, int count, String imgUrl, Long itemId){
        this.cartItemId = cartItemId;
        this.itemName = itemName;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
        this.itemId=itemId;
    }

}

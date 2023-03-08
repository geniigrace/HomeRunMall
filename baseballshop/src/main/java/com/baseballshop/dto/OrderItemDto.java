package com.baseballshop.dto;

import com.baseballshop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    private String itemName;

    private int count;

    private int orderPrice;

    private int orderItemTotalPrice;

    private String imgUrl;

    public OrderItemDto(OrderItem orderItem, String imgUrl){
        this.itemName = orderItem.getItem().getItemName();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.orderItemTotalPrice = orderItem.getOrderItemTotalPrice();
        this.imgUrl = imgUrl;
    }
}
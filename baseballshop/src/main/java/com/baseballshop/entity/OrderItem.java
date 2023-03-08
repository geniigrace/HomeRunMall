package com.baseballshop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

    private int orderItemTotalPrice;

    //주문하기를 위한 추가
    public static OrderItem createOrderItem(Item item, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());
        orderItem.setOrderItemTotalPrice(item.getPrice()*count);
        item.removeStock(count);
        return orderItem;

    }

    public int getTotalPrice() {
        return orderPrice * count;
    }

    //주문내역 > 주문취소를 위한 추가
    public void cancel() {
        this.getItem().addStock(count);
    }
}
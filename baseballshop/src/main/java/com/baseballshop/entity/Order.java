package com.baseballshop.entity;

import com.baseballshop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    // OrderItem에 있는 Order에 의해 관리
    private List<OrderItem> orderItems = new ArrayList<>();

    private int orderTotalPrice;

    private LocalDateTime regTime;

    private LocalDateTime updateTime;

    //주문하기를 위한 추가
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        Order order = new Order();
        order.setMember(member);
        for (OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER);

        order.setOrderDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm")));

        int totalOrderPrice=0;
        for(OrderItem orderItem : orderItemList){
            totalOrderPrice += orderItem.getOrderItemTotalPrice();
        }
        order.setOrderTotalPrice(totalOrderPrice);
        return order;
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getOrderItemTotalPrice();
        }

        return totalPrice;
    }

    //주문내역 > 주문취소를 위한 추가
    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;

        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();

        }
    }
}
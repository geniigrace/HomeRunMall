package com.shop.repository;

import com.shop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o " +
            "where o.member.userID = :userID " +
            "order by o.orderDate desc"
    )
    List<Order> findOrders(@Param("userID") String userID, Pageable pageable);

    @Query("select count(o) from Order o " +
            "where o.member.userID = :userID"
    )
    Long countOrder(@Param("userID") String userID);
}
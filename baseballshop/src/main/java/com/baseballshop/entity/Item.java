package com.baseballshop.entity;

import com.baseballshop.constant.ItemCategory;
import com.baseballshop.constant.SellStatus;
import com.baseballshop.constant.ShowStatus;
import com.baseballshop.constant.Team;
import com.baseballshop.dto.ItemFormDto;
import com.baseballshop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SellStatus sellStatus;

    @Enumerated(EnumType.STRING)
    private ShowStatus showStatus;

    private Team team;

    private ItemCategory category;

    @Column(nullable = false, length = 50)
    private String itemName;

    @Column(name = "price", nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockNumber;

    @Lob
    @Column(nullable = false)
    private String itemDetail;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name="member_item",
            joinColumns = @JoinColumn(name="member_id"),
            inverseJoinColumns = @JoinColumn(name="item_id")
    )
    private List<Member> member;

    //상품 수정시
    public void updateItem(ItemFormDto itemFormDto){
        this.sellStatus = itemFormDto.getSellStatus();
        this.team = itemFormDto.createItem().getTeam();
        this.category = itemFormDto.createItem().getCategory();
        this.showStatus = itemFormDto.getShowStatus();

        this.itemName = itemFormDto.getItemName();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
    }

    //주문시 재고
    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber;

        if(restStock < 0){
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량 : " + this.stockNumber + " )");
        }

        this.stockNumber = restStock;
    }

    //주문취소시
    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
    }

}

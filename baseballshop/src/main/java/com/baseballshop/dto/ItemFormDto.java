package com.baseballshop.dto;

import com.baseballshop.constant.SellStatus;
import com.baseballshop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {

    private Long id;

    private SellStatus sellStatus;

    @NotEmpty(message = "구단을 선택하세요.")
    private String team;

    @NotEmpty(message = "상품 종류를 선택하세요.")
    private String category;

    @NotEmpty(message = "상품명을 입력하세요.")
    private String itemName;

    @NotNull(message = "상품 가격을 입력하세요.")
    private Integer price;

    @NotNull(message = "재고 수량을 입력하세요.")
    private Integer stockNumber;

    @NotEmpty(message = "상품 상세내용을 입력하세요.")
    private String itemDetail;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>(); //상품 이미지 정보

    private List<Long> itemImgIds = new ArrayList<>(); //상품 이미지 아이디

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class); //ItemFormDto -> Item 연결
    }

    public static ItemFormDto of(Item item){
        return modelMapper.map(item, ItemFormDto.class); //Item -> ItemFormDto 연결
    }

}

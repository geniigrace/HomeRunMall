package com.baseballshop.dto;

import com.baseballshop.constant.ItemCategory;
import com.baseballshop.constant.SellStatus;
import com.baseballshop.constant.ShowStatus;
import com.baseballshop.constant.Team;
import com.baseballshop.entity.Item;
import lombok.Getter;
import lombok.Setter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {

    private Long id;

    private SellStatus sellStatus;

    @NotNull(message = "상품 등록중 에러가 발생하였습니다.")
    private ShowStatus showStatus;

    @NotNull(message = "구단을 선택하세요.")
    private Team team;

    @NotNull(message = "상품 종류를 선택하세요.")
    private ItemCategory category;

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

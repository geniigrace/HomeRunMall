package com.baseballshop.repository;

import com.baseballshop.constant.ItemCategory;
import com.baseballshop.constant.SellStatus;

import com.baseballshop.constant.ShowStatus;
import com.baseballshop.constant.Team;
import com.baseballshop.dto.*;
import com.baseballshop.entity.QItem;
import com.baseballshop.entity.QItemImg;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.baseballshop.entity.Item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;


public class ItemRepositoryCustomImpl  implements ItemRepositoryCustom {


    private JPAQueryFactory queryFactory;

     public ItemRepositoryCustomImpl(EntityManager em){
       this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(SellStatus searchSellStatus){
        return searchSellStatus == null ? null : QItem.item.sellStatus.eq(searchSellStatus);
    }

    private BooleanExpression searchShowStatus(ShowStatus showStatus){
         return showStatus == null ? null : QItem.item.showStatus.eq(showStatus);
    }

    private BooleanExpression searchTeam(Team searchTeam){

         if(StringUtils.equals("", searchTeam) || searchTeam ==null ){

             return null;
         }
         else{
             return QItem.item.team.eq(searchTeam);
         }

    }
    private BooleanExpression searchCategory(ItemCategory searchCategory){
       if(StringUtils.equals("", searchCategory) || searchCategory == null) {
           return null;
       }
       else {
           return QItem.item.category.eq(searchCategory);
       }
    }

    private BooleanExpression regDtsAfter(String searchDateType){

        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        }
        else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        }
        else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        }
        else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        }
        else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);

    }

    private BooleanExpression searchByLike(String searchQuery){
        if(searchQuery != null){
            return QItem.item.itemName.like("%"+searchQuery+"%");
        }
        else {
            return null;
        }
    }

    private BooleanExpression searchItemCategory(String itemCategory){
         ItemCategory itemCategoryEnum = ItemCategory.valueOf(itemCategory);
        return itemCategory == null ? null : QItem.item.category.eq(itemCategoryEnum);
    }

    private BooleanExpression searchItemTeam(String team){
         Team itemTeamEnum = Team.valueOf(team);
         return team == null ? null : QItem.item.team.eq(itemTeamEnum);
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        QueryResults<Item> results = queryFactory.select(QItem.item)
                .from(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchTeam(itemSearchDto.getSearchTeam()),
                        searchCategory(itemSearchDto.getSearchCategory()),
                        searchByLike(itemSearchDto.getSearchQuery()),
                        searchShowStatus(ShowStatus.SHOW))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset()).
                limit(pageable.getPageSize()).fetchResults();

        List<Item> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    //메인페이지 아이템 노출
    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        //QMainItemDto @QueryProjection 을 하면 DTO로 바로 조회 가능
        QueryResults<MainItemDto> results
                = queryFactory.select(new QMainItemDto(item.id, item.itemName, item.itemDetail, itemImg.imgUrl, item.price))
                .from(itemImg).join(itemImg.item, item).where(itemImg.repImgYn.eq("Y"))
                .where(itemNameLike(itemSearchDto.getSearchQuery()),
                        searchShowStatus(ShowStatus.SHOW))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    //메인화면 관련
    private BooleanExpression itemNameLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemName.like("%" + searchQuery + "%");
    }

    //상품리스트 페이지
    @Override
    public Page<ItemListDto> getItemListPage(String itemCategory,Pageable pageable){
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<ItemListDto> results
                = queryFactory.select(new QItemListDto(item.id, item.itemName,itemImg.imgUrl, item.price, item.category))
                .from(itemImg).join(itemImg.item, item).where(itemImg.repImgYn.eq("Y"))
                .where(searchItemCategory(itemCategory))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).fetchResults();

        List<ItemListDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<ItemListDto> getTeamItemListPage(String itemCategory,String team, Pageable pageable){
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<ItemListDto> results
                = queryFactory.select(new QItemListDto(item.id, item.itemName,itemImg.imgUrl, item.price, item.category))
                .from(itemImg).join(itemImg.item, item).where(itemImg.repImgYn.eq("Y"))
                .where(searchItemCategory(itemCategory), searchItemTeam(team))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).fetchResults();

        List<ItemListDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

}

package com.baseballshop.repository;

import com.baseballshop.constant.SellStatus;

import com.baseballshop.entity.QItem;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.baseballshop.dto.ItemSearchDto;
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

    private BooleanExpression searchTeam(String searchTeam){

         if(StringUtils.equals("", searchTeam) || searchTeam ==null ){

             return null;
         }
         else{
             return QItem.item.team.eq(searchTeam);
         }

    }

    private BooleanExpression searchCategory(String searchCategory){
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

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("itemName", searchBy)){
            return QItem.item.itemName.like("%"+searchQuery+"%");
        }
        else {
            return null;
        }
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        QueryResults<Item> results = queryFactory.select(QItem.item)
                .from(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchTeam(itemSearchDto.getSearchTeam()),
                        searchCategory(itemSearchDto.getSearchCategory()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()) )
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset()).
                limit(pageable.getPageSize()).fetchResults();

        List<Item> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

}

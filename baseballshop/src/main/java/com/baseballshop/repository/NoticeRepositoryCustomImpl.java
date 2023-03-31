package com.baseballshop.repository;

import com.baseballshop.constant.NoticeStatus;
import com.baseballshop.constant.ShowStatus;
import com.baseballshop.dto.NoticeSearchDto;
import com.baseballshop.entity.Notice;
import com.baseballshop.entity.QNotice;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class NoticeRepositoryCustomImpl implements NoticeRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public NoticeRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }


    private BooleanExpression searchNoticeTypeEq(NoticeStatus searchNoticeType){
        return searchNoticeType == null ? null : QNotice.notice.noticeType.eq(searchNoticeType);
    }

    private BooleanExpression searchShowStatusEq(ShowStatus showStatus){
        return showStatus == null ? null : QNotice.notice.showStatus.eq(showStatus);
    }

    //등록시기
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

        return QNotice.notice.createTime.after(dateTime);


    }

    //게시글 제목
    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("noticeTitle", searchBy)){
            return QNotice.notice.noticeTitle.like("%"+searchQuery+"%");
        }
        return null;
    }

    @Override
    public Page<Notice> getNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable){

        QueryResults<Notice> results = queryFactory.selectFrom(QNotice.notice)
                .where(regDtsAfter(noticeSearchDto.getSearchDateType()),
                        searchNoticeTypeEq(noticeSearchDto.getSearchNoticeType()),
                        searchByLike(noticeSearchDto.getSearchBy(), noticeSearchDto.getSearchQuery()),
                        searchShowStatusEq(ShowStatus.SHOW))
                .orderBy(QNotice.notice.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).fetchResults();

        List<Notice> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);

    }
}

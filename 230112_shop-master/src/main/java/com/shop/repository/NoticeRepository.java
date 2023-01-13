package com.shop.repository;


import com.shop.entity.Item;
import com.shop.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;


public interface NoticeRepository extends JpaRepository<Notice, Long>, QuerydslPredicateExecutor<Item>, NoticeRepositoryCustom  {


    //상세페이지
    //List<Notice> findByNoticeDetail(String noticeDetail);

    //List<Notice> findByNoticeId(Long noticeId);
}

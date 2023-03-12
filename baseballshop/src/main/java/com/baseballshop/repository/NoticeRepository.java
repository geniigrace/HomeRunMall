package com.baseballshop.repository;

import com.baseballshop.entity.Item;
import com.baseballshop.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface NoticeRepository extends JpaRepository<Notice, Long>, QuerydslPredicateExecutor<Notice>, NoticeRepositoryCustom  {



}

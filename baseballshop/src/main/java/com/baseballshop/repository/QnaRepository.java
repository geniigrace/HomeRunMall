package com.baseballshop.repository;

import com.baseballshop.dto.QnaListDto;
import com.baseballshop.entity.Qna;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface QnaRepository extends JpaRepository<Qna,Long>, QuerydslPredicateExecutor<Qna>{

    List<Qna> findAllByOrderByIdDesc(Pageable pageable);

}

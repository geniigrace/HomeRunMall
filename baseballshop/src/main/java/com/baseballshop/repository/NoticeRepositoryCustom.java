package com.baseballshop.repository;

import com.baseballshop.dto.NoticeSearchDto;
import com.baseballshop.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {

    Page<Notice> getNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable);

}

package com.baseballshop.controller;

import com.baseballshop.dto.NoticeDeleteDto;
import com.baseballshop.dto.NoticeDto;
import com.baseballshop.dto.NoticeFormDto;
import com.baseballshop.dto.NoticeSearchDto;
import com.baseballshop.entity.Notice;
import com.baseballshop.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class CommunityController {

    //커뮤니티
    @GetMapping(value = "/community")
    public String community(){
        return "community/community";
    }

    //공지사항
    private final NoticeService noticeService;

    //공지사항 리스트
    @GetMapping(value = "/notice")
    public String noticeManage(NoticeSearchDto noticeSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<Notice> notices = noticeService.getNoticePage(noticeSearchDto,pageable);

        model.addAttribute("notices", notices);
        model.addAttribute("noticeSearchDto", noticeSearchDto);
        model.addAttribute("maxPage", 5);

        return "community/notice";
    }



    //공지사항 글보기
    @GetMapping(value = "/notice/{noticeId}")
    public String noticeDtl(@PathVariable("noticeId")Long noticeId, Model model){

        NoticeFormDto noticeFormDto = noticeService.preNotice(noticeId);

        model.addAttribute("notices", noticeFormDto);
        model.addAttribute("newLineChar", '\n');
        return "community/noticeDtl";
    }

}

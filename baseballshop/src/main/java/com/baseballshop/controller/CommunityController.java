package com.baseballshop.controller;

import com.baseballshop.dto.*;
import com.baseballshop.entity.Notice;
import com.baseballshop.service.LoginUserService;
import com.baseballshop.service.NoticeService;
import com.baseballshop.service.QnaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class CommunityController {

    private final QnaService qnaService;
    private final LoginUserService loginUserService;
    private final NoticeService noticeService;

    //공지사항 리스트
    @GetMapping(value = "/notice")
    public String noticeManage(NoticeSearchDto noticeSearchDto, @PathVariable("page") Optional<Integer> page,  Model model, Principal principal){

        if(principal!=null){
            String loginName = loginUserService.loginUserNameEmail(principal)[0];
            model.addAttribute("loginName", loginName);
        }

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Notice> notices = noticeService.getNoticePage(noticeSearchDto,pageable);

        model.addAttribute("notices", notices);
        model.addAttribute("noticeSearchDto", noticeSearchDto);
        model.addAttribute("maxPage", 10);

        return "notice/notice";
    }

    //공지사항 글보기
    @GetMapping(value = "/notice/{noticeId}")
    public String noticeDtl(@PathVariable("noticeId")Long noticeId,  Model model, Principal principal){

        if(principal!=null){
            String loginName = loginUserService.loginUserNameEmail(principal)[0];
            model.addAttribute("loginName", loginName);
        }

        NoticeFormDto noticeFormDto = noticeService.preNotice(noticeId);

        model.addAttribute("notices", noticeFormDto);
        model.addAttribute("newLineChar", '\n');
        return "notice/noticeDtl";
    }

    //QNA
    @GetMapping(value = {"/qna", "/qna/{page}"})
    public String qna(@PathVariable("page") Optional<Integer> page,  Model model, Principal principal){

        if(principal!=null){
            String loginName = loginUserService.loginUserNameEmail(principal)[0];
            model.addAttribute("loginName", loginName);
        }
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        Page<QnaListDto> qna = qnaService.getQnaPage(pageable);

        model.addAttribute("qna", qna);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "user/qna";
    }

    //QNA 등록페이지
    @GetMapping(value = "/qna/new")
    public String qnaForm(Model model, Principal principal){


        if(principal!=null){
            String loginName = loginUserService.loginUserNameEmail(principal)[0];
            model.addAttribute("loginName", loginName);

            String loginEmail = loginUserService.loginUserNameEmail(principal)[1];
            QnaFormDto qnaFormDto = new QnaFormDto();
            qnaFormDto.setQnaEmail(loginEmail);
            model.addAttribute("qnaFormDto",qnaFormDto);

            return "user/qnaForm";
        }
        else {
            return "member/memberLoginForm";
        }
    }

    //QNA 등록
    @PostMapping(value = "/qna/new")
    public String qnaNew(@Valid QnaFormDto qnaFormDto, BindingResult bindingResult, Model model, Principal principal){
        if(bindingResult.hasErrors()){
            return "user/qnaForm";
        }
        try{
            qnaService.saveQna(qnaFormDto, principal);
        }
        catch (Exception e){
            model.addAttribute("errorMessage", "게시글 등록중 에러가 발생했습니다.");
            return "user/qnaForm";
        }

        return "redirect:/qna";
    }

    //QNA 답변완료상태 변경
    @PutMapping("/qna/{id}/done")
    public @ResponseBody ResponseEntity doneQna(@PathVariable("id") Long qnaId,Principal principal){

        Long qnaDoneId = qnaService.qnaDone(qnaId);

        return new ResponseEntity<Long>(qnaDoneId, HttpStatus.OK);
    }
}

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

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class CommunityController {

    private final QnaService qnaService;
    private final LoginUserService loginUserService;
    private final NoticeService noticeService;

    //공지사항 리스트
    @GetMapping(value = {"/notice", "/notice/list/{page}"})
    public String noticeManage(NoticeSearchDto noticeSearchDto, @PathVariable("page") Optional<Integer> page,  Model model, Principal principal){

        if(principal!=null){
            String loginName = loginUserService.loginUserNameEmail(principal)[0];
            model.addAttribute("loginName", loginName);
        }

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<Notice> notices = noticeService.getNoticePage(noticeSearchDto,pageable);

        model.addAttribute("notices", notices);
        //model.addAttribute("noticeSearchDto", noticeSearchDto);
        model.addAttribute("maxPage", 3);

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
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);

        Page<QnaListDto> qna = qnaService.getQnaPage(pageable);

        model.addAttribute("qna", qna);
        model.addAttribute("maxPage", 3);

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

            //model.addAttribute("check", false);
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

    //QNA수정페이지
    @GetMapping(value = "/qna/modify/{id}")
    public String qnaUpdatePage(@PathVariable("id")Long qnaId, Model model, Principal principal){

        String loginName = loginUserService.loginUserNameEmail(principal)[0];
        model.addAttribute("loginName", loginName);

        QnaListDto qnaListDto = qnaService.qnaView(qnaId);
//        Boolean check = qnaService.qnaWriterCheck(loginUserService.loginUserNameEmail(principal)[1], qnaId);

//        model.addAttribute("check", check);
        model.addAttribute("qnaFormDto", qnaListDto);
        model.addAttribute("newLineChar", '\n');
        return "user/qnaForm";
    }

    @PostMapping(value = "/qna/modify/{id}")
    public String qnaUpdate (@Valid QnaFormDto qnaFormDto, BindingResult bindingResult, Model model, HttpServletResponse response){
        if(bindingResult.hasErrors()){
            return "user/qnaForm";
        }
        try{
            qnaService.updateQna(qnaFormDto);

            response.setContentType("text/html; charset=euc-kr");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('게시글이 수정되었습니다.'); location.href='/qna';</script>");
            out.flush();
            return "redirect:/qna";
        }
        catch(Exception e){
            model.addAttribute("errorMessage", "게시글 수정 중 에러가 발생하였습니다.");
            return "user/qnaForm";
        }
    }
    //QNA 답변완료상태 변경
    @PutMapping(value="/qna/done/{id}")
    public @ResponseBody ResponseEntity doneQna(@PathVariable("id") Long qnaId,Principal principal){

        Long qnaDoneId = qnaService.qnaDone(qnaId);

        return new ResponseEntity<Long>(qnaDoneId, HttpStatus.OK);
    }

    @PostMapping(value = "/qna/memberCheck/{id}")
    public @ResponseBody ResponseEntity qnaMemberCheck(@PathVariable("id")Long qnaId, Principal principal){
       if(principal != null) {
           //로그인한 이메일
           String loginEmail = loginUserService.loginUserNameEmail(principal)[1];
           //비교
           Boolean result =qnaService.qnaWriterCheck(loginEmail, qnaId);
           return new ResponseEntity<Boolean>(result, HttpStatus.OK);
       }
       else{
           return new ResponseEntity<Integer>(HttpStatus.UNAUTHORIZED);
       }
    }

    @GetMapping(value="/qna/detail/{id}")
    public String qnaDtl(@PathVariable("id")Long qnaId, Model model, Principal principal){
        if(principal!=null){
            String loginName = loginUserService.loginUserNameEmail(principal)[0];
            model.addAttribute("loginName", loginName);
        }

        QnaListDto qnaListDto = qnaService.qnaView(qnaId);
        Boolean check = qnaService.qnaMemberCheck(loginUserService.loginUserNameEmail(principal)[1], qnaId);

        model.addAttribute("check", check);
        model.addAttribute("qna", qnaListDto);
        model.addAttribute("newLineChar", '\n');
        return "user/qnaDtl";
    }

}

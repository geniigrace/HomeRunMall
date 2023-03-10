package com.baseballshop.controller;

import com.baseballshop.config.CustomOAuth2UserService;
import com.baseballshop.dto.*;
import com.baseballshop.entity.Member;
import com.baseballshop.entity.Notice;
import com.baseballshop.repository.MemberRepository;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class CommunityController {

    private final MemberRepository memberRepository;
    private final QnaService qnaService;
    private final NoticeService noticeService;
    private final HttpSession httpSession;
    //커뮤니티
    @GetMapping(value = "/community")
    public String community( Model model, Principal principal){

        if(principal!=null){
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                SessionUser user = (SessionUser)httpSession.getAttribute("member");
                model.addAttribute("loginName", user.getName());

            } else {
                model.addAttribute("loginName", member.getName());
            }
        }

        return "community/community";
    }

    //공지사항 리스트
    @GetMapping(value = "/notice")
    public String noticeManage(NoticeSearchDto noticeSearchDto, @PathVariable("page") Optional<Integer> page,  Model model, Principal principal){

        if(principal!=null){
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                SessionUser user = (SessionUser)httpSession.getAttribute("member");
                model.addAttribute("loginName", user.getName());

            } else {
                model.addAttribute("loginName", member.getName());
            }
        }

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);
        Page<Notice> notices = noticeService.getNoticePage(noticeSearchDto,pageable);

        model.addAttribute("notices", notices);
        model.addAttribute("noticeSearchDto", noticeSearchDto);
        model.addAttribute("maxPage", 10);

        return "community/notice";
    }



    //공지사항 글보기
    @GetMapping(value = "/notice/{noticeId}")
    public String noticeDtl(@PathVariable("noticeId")Long noticeId,  Model model, Principal principal){

        if(principal!=null){
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                SessionUser user = (SessionUser)httpSession.getAttribute("member");
                model.addAttribute("loginName", user.getName());

            } else {
                model.addAttribute("loginName", member.getName());
            }
        }

        NoticeFormDto noticeFormDto = noticeService.preNotice(noticeId);

        model.addAttribute("notices", noticeFormDto);
        model.addAttribute("newLineChar", '\n');
        return "community/noticeDtl";
    }

    //QNA
    @GetMapping(value = "/qna")
    public String qna( Model model, Principal principal){

        if(principal!=null){
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                SessionUser user = (SessionUser)httpSession.getAttribute("member");
                model.addAttribute("loginName", user.getName());

            } else {
                model.addAttribute("loginName", member.getName());
            }
        }

        return "community/qna";
    }

    //QNA 등록페이지
    @GetMapping(value = "/qna/new")
    public String qnaForm(Model model, Principal principal){
        String email="";

        if(principal!=null){
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                SessionUser user = (SessionUser)httpSession.getAttribute("member");
                email=user.getEmail();
                model.addAttribute("loginName", user.getName());

            } else {
                email=member.getEmail();
                model.addAttribute("loginName", member.getName());
            }
        }
        QnaFormDto qnaFormDto = new QnaFormDto();
        qnaFormDto.setQnaEmail(email);

        model.addAttribute("qnaFormDto",qnaFormDto);

        return "community/qnaForm";
    }

    //QNA 등록
    @PostMapping(value = "/qna/new")
    public String qnaNew(@Valid QnaFormDto qnaFormDto, BindingResult bindingResult, Model model, Principal principal){
        if(bindingResult.hasErrors()){
            return "community/qna";
        }

        try{
            qnaService.saveQna(qnaFormDto, principal);
        }
        catch (Exception e){
            model.addAttribute("errorMessage", "게시글 등록중 에러가 발생했습니다.");
            return "community/qna";
        }

        return "redirect:/qna";

    }
}

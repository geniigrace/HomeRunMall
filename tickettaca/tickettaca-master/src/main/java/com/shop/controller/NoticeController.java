package com.shop.controller;

import com.shop.dto.NoticeFormDto;
import com.shop.dto.NoticeSearchDto;
import com.shop.entity.Notice;
import com.shop.service.MemberService;
import com.shop.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    private final MemberService memberService;

    //공지사항 리스트
    @GetMapping(value = {"/notice"})
    public String noticeManage(NoticeSearchDto noticeSearchDto, @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<Notice> notices = noticeService.getNoticePage(noticeSearchDto,pageable);

        model.addAttribute("notices", notices);
        model.addAttribute("noticeSearchDto", noticeSearchDto);
        model.addAttribute("maxPage", 5);

        return "notice/noticeMng";
    }


    //공지사항 등록하기
    @GetMapping(value = "/notice/new")
    public String noticeForm(Model model){
        model.addAttribute("noticeFormDto", new NoticeFormDto());
        return "notice/noticeForm";
    }

    @PostMapping(value = "/notice/new")
    public String noticeNew(@Valid NoticeFormDto noticeFormDto,
                            BindingResult bindingResult,
                            Model model,
                            @RequestParam("noticeImgFile") List<MultipartFile> noticeImgFileList){
        if(bindingResult.hasErrors()){
            return "notice/noticeForm";
        }

        try{
            noticeService.saveNotice(noticeFormDto, noticeImgFileList);
        }
        catch (Exception e){
            model.addAttribute("errorMessage", "게시글 등록중 에러가 발생했습니다.");
            return "notice/noticeForm";
        }

        return "redirect:/notice";
    }


    //공지사항 수정 : 내용
    @GetMapping(value = "/notice/admin/{noticeId}")
    public String noticeUpdate(@PathVariable("noticeId")Long noticeId, Model model){

        try{
            NoticeFormDto noticeFormDto = noticeService.preNotice(noticeId);
            model.addAttribute("noticeFormDto", noticeFormDto); //View로 보내기 위해 모델 추가
        }
        catch (EntityNotFoundException e){
            model.addAttribute("errorMessage", "존재하지 않는 게시글 입니다.");
            model.addAttribute("noticeFormDto", "new NoticeFormDto");
            return "notice/noticeForm";
        }

        return "notice/noticeForm";
    }

    // 공지사항 수정 : 이미지
    @PostMapping(value = "/notice/admin/{noticeId}")
    public String noticeUpdate(@Valid NoticeFormDto noticeFormDto,
                               BindingResult bindingResult,
                               @RequestParam("noticeImgFile") List<MultipartFile> noticeImgFileList,
                               Model model){

        if(bindingResult.hasErrors()){
            return "notice/noticeForm";
        }

        if(noticeImgFileList.get(0).isEmpty() && noticeFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "notice/noticeForm";
        }

        try{
            noticeService.updateNotice(noticeFormDto, noticeImgFileList);
        }
        catch(Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "notice/noticeForm";
        }

        return "redirect:/notice";
    }

    @GetMapping(value = "/notice/{noticeId}")
    public String noticeDtl(@PathVariable("noticeId")Long noticeId, Model model){

        NoticeFormDto noticeFormDto = noticeService.preNotice(noticeId);

        model.addAttribute("notice", noticeFormDto);
        model.addAttribute("newLineChar", '\n');
        return "notice/noticeDtl";
    }

}

package com.baseballshop.controller;

import com.baseballshop.config.CustomOAuth2UserService;
import com.baseballshop.dto.*;
import com.baseballshop.entity.Item;
import com.baseballshop.entity.Member;
import com.baseballshop.entity.Notice;
import com.baseballshop.entity.Order;
import com.baseballshop.repository.ItemRepository;
import com.baseballshop.repository.MemberRepository;
import com.baseballshop.repository.NoticeRepository;
import com.baseballshop.repository.OrderRepository;
import com.baseballshop.service.ItemService;
import com.baseballshop.service.LoginUserService;
import com.baseballshop.service.NoticeService;
import com.baseballshop.service.OrderService;
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
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final ItemService itemService;
    private final NoticeService noticeService;
    private final OrderService orderService;
    private final LoginUserService loginUserService;

    //관리페이지
    @GetMapping(value = "/adminpage")
    public String adminpage( Model model, Principal principal){
        if(principal!=null){//로그인 했을 때
            String loginName = loginUserService.loginUserNameEmail(principal)[0];
            model.addAttribute("loginName", loginName);
            return "admin/adminpage";
        }
        else {
            return "member/memberLoginForm";
        }
    }

    //상품등록
    @GetMapping(value ="/item/new")
    public String itemForm(Model model,Principal principal){
        String loginName = loginUserService.loginUserNameEmail(principal)[0];
        model.addAttribute("loginName", loginName);

        model.addAttribute("itemFormDto", new ItemFormDto());

        return "admin/itemForm";
    }

    @PostMapping(value = "/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto,
                          BindingResult bindingResult,
                          Model model, HttpServletResponse response,
                          @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){

        if(bindingResult.hasErrors()){
            return "admin/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다."); //itemForm.html 윗단에 에러 aleter 으로 뜸
            return "admin/itemForm";
        }
        try{
            itemService.saveItem(itemFormDto, itemImgFileList);
            response.setContentType("text/html; charset=euc-kr");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('상품등록이 완료되었습니다.'); location.href='/admin/items'; </script>");
            out.flush();
        }
        catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록중 에러가 발생하였습니다.");
            return "admin/itemForm";
        }

        return "redirect:/admin/items";
    }

    //상품관리
    @GetMapping(value = {"/items", "/items/{page}"}) //상품관리리스트, 상품 수정페이지
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model, Principal principal){

        if(principal!=null){

            String loginName = loginUserService.loginUserNameEmail(principal)[0];
            model.addAttribute("loginName", loginName);

            if (itemSearchDto.getSearchQuery() == null || itemSearchDto.getSearchQuery().equals("undefined")) {
                itemSearchDto.setSearchQuery("");
            }

            Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);

            Page<ItemsDto> items = itemService.getAdminItemPage(itemSearchDto, pageable);

            model.addAttribute("items", items);
            model.addAttribute("itemSearchDto", itemSearchDto);
            model.addAttribute("maxPage", 4);

            return "admin/items";
        }
        else {
            return "member/memberLoginForm";
        }
    }

    //상품 수정
    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(@PathVariable("itemId")Long itemId, Model model, Principal principal){
        if(principal!=null){
            String loginName = loginUserService.loginUserNameEmail(principal)[0];
            model.addAttribute("loginName", loginName);
            try{
                ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
                model.addAttribute("itemFormDto", itemFormDto); //View로 보내기 위해 모델 추가
            }
            catch (EntityNotFoundException e){
                model.addAttribute("errorMessage", "존재하지 않는 상품입니다.");
                model.addAttribute("itemFormDto", "new ItemFormDto");
                return "item/itemForm";
            }

            return "admin/itemForm";
        }
        else {
            return "member/memberLoginForm";
        }
    }

    @PostMapping(value = "/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto,
                             BindingResult bindingResult,HttpServletResponse response,
                             @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList,
                             Model model){

        if(bindingResult.hasErrors()){
            return "admin/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "admin/itemForm";
        }

        try{
            itemService.updateItem(itemFormDto, itemImgFileList);
            response.setContentType("text/html; charset=euc-kr");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('상품정보가 수정되었습니다.'); location.href='/admin/items'; </script>");
            out.flush();
        }
        catch(Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "admin/itemForm";
        }

        return "redirect:/admin/items";
    }

    //상품 삭제
    //삭제기능 구현은 했지만 img와 외래키로 연결되어있어 작성하는 글 삭제 하지않고 구분자만 지정하여 구분자를 Delete로 두고 hide 시켜 구현하였음
    @PutMapping(value = "/item/hidden")
    public @ResponseBody ResponseEntity modifyDelItem(@RequestBody ItemDeleteDto itemDeleteDto){

        List<ItemDeleteDto> itemDeleteDtoList = itemDeleteDto.getItemDeleteDtoList();

        if(itemDeleteDtoList == null || itemDeleteDtoList.size()==0){
            return new ResponseEntity<String>("삭제할 상품을 선택하세요.", HttpStatus.FORBIDDEN);
        }

        Long itemId = itemService.modify(itemDeleteDto);

        return new ResponseEntity<Long>(itemId, HttpStatus.OK);
    }

    //주문관리 : 전체주문내역
    @GetMapping(value = {"/orders","/orders/{page}"})
    public String orders(Model model, @PathVariable("page") Optional<Integer> page, Principal principal ){

        if(principal!=null){
            String loginName = loginUserService.loginUserNameEmail(principal)[0];
            model.addAttribute("loginName", loginName);
            Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

            Page<OrderHistDto> orderHistDtoList = orderService.getAllOrderList(pageable);

            model.addAttribute("orders", orderHistDtoList);
            model.addAttribute("maxPage", 10);

            return "admin/orders";
        }
        else {
            return "member/memberLoginForm";
        }
    }

    //공지사항
    //등록하기
    @GetMapping(value = "/notice/new")
    public String noticeForm(Model model, Principal principal){

        if(principal!=null){
            String loginName = loginUserService.loginUserNameEmail(principal)[0];
            model.addAttribute("loginName", loginName);
            model.addAttribute("noticeFormDto", new NoticeFormDto());
            return "admin/noticeForm";
        }
        else {
            return "member/memberLoginForm";
        }
    }

    @PostMapping(value = "/notice/new")
    public String noticeNew(@Valid NoticeFormDto noticeFormDto,
                            BindingResult bindingResult,
                            Model model,HttpServletResponse response,
                            @RequestParam("noticeImgFile") List<MultipartFile> noticeImgFileList){
        if(bindingResult.hasErrors()){
            return "admin/noticeForm";
        }

        try{
            noticeService.saveNotice(noticeFormDto, noticeImgFileList);
            response.setContentType("text/html; charset=euc-kr");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('게시글이 등록되었습니다.'); location.href='/notice';</script>");
            out.flush();
        }
        catch (Exception e){
            model.addAttribute("errorMessage", "게시글 등록중 에러가 발생했습니다.");
            return "admin/noticeForm";
        }

        return "notice/notice";
    }


    //공지사항 수정페이지
    @GetMapping(value = "/notice/{noticeId}")
    public String noticeUpdate(@PathVariable("noticeId")Long noticeId, Model model, Principal principal){

        if(principal!=null){
            String loginName = loginUserService.loginUserNameEmail(principal)[0];
            model.addAttribute("loginName", loginName);
            try{
                NoticeFormDto noticeFormDto = noticeService.preNotice(noticeId);
                model.addAttribute("noticeFormDto", noticeFormDto); //View로 보내기 위해 모델 추가
            }
            catch (EntityNotFoundException e){
                model.addAttribute("errorMessage", "존재하지 않는 게시글 입니다.");
                model.addAttribute("noticeFormDto", "new NoticeFormDto");
                return "admin/noticeForm";
            }

            return "admin/noticeForm";
        }
        else {
            return "member/memberLoginForm";
        }
    }

    // 공지사항 수정한 내용 저장
    @PostMapping(value = "/notice/{noticeId}")
    public String noticeUpdate(@Valid NoticeFormDto noticeFormDto, HttpServletResponse response, BindingResult bindingResult, @RequestParam("noticeImgFile") List<MultipartFile> noticeImgFileList, Model model){

        if(bindingResult.hasErrors()){
            return "admin/noticeForm";
        }

        try{
            noticeService.updateNotice(noticeFormDto, noticeImgFileList);
            response.setContentType("text/html; charset=euc-kr");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('게시글이 수정되었습니다.'); location.href='/notice';</script>");
            out.flush();
        }
        catch(Exception e){
            model.addAttribute("errorMessage", "게시글 수정 중 에러가 발생했습니다."+e.getMessage());
            return "admin/noticeForm";
        }

        return "notice/notice";
    }

    //공지사항 삭제
    @DeleteMapping(value = "/notice/hidden")
    public @ResponseBody ResponseEntity modifyNotice(@RequestBody NoticeDeleteDto noticeDeleteDto){

        List<NoticeDeleteDto> noticeDeleteDtoList = noticeDeleteDto.getNoticeDeleteDtoList();

        if(noticeDeleteDtoList == null || noticeDeleteDtoList.size()==0){
            if(noticeDeleteDtoList == null){
            }
            return new ResponseEntity<String>("삭제할 게시글을 선택하세요.", HttpStatus.FORBIDDEN);
        }

        Long noticeId = noticeService.delete(noticeDeleteDto);

        return new ResponseEntity<Long>(noticeId, HttpStatus.OK);
    }

    //삭제기능 구현은 했지만 img와 외래키로 연결되어있어 작성하는 글 삭제 하지않고 구분자만 지정하여 구분자를 Delete로 두고 hide 시켜 구현하였음
//    @PutMapping(value = "/notice/hidden")
//    public @ResponseBody ResponseEntity modifyNotice(@RequestBody NoticeDeleteDto noticeDeleteDto){
//
//        List<NoticeDeleteDto> noticeDeleteDtoList = noticeDeleteDto.getNoticeDeleteDtoList();
//
//        if(noticeDeleteDtoList == null || noticeDeleteDtoList.size()==0){
//            if(noticeDeleteDtoList == null){
//            }
//            return new ResponseEntity<String>("삭제할 게시글을 선택하세요.", HttpStatus.FORBIDDEN);
//        }
//
//        Long noticeId = noticeService.modify(noticeDeleteDto);
//
//        return new ResponseEntity<Long>(noticeId, HttpStatus.OK);
//    }

    //회원관리
    @GetMapping(value = "/members" )
    public String memberList(Model model, Principal principal){

        if(principal!=null){
            String loginName = loginUserService.loginUserNameEmail(principal)[0];
            model.addAttribute("loginName", loginName);
            return "admin/members";
        }
        else {
            return "member/memberLoginForm";
        }
    }
}

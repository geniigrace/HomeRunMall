package com.baseballshop.controller;

import com.baseballshop.dto.*;
import com.baseballshop.entity.Item;
import com.baseballshop.entity.Notice;
import com.baseballshop.repository.ItemRepository;
import com.baseballshop.repository.NoticeRepository;
import com.baseballshop.service.ItemService;
import com.baseballshop.service.NoticeService;
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
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final NoticeService noticeService;
    private final NoticeRepository noticeRepository;

    //관리페이지
    @GetMapping(value = "/adminpage")
    public String adminpage(){
        return "admin/adminpage";
    }

    //상품등록
    @GetMapping(value ="/item/new")
    public String itemForm(Model model){

        model.addAttribute("itemFormDto", new ItemFormDto());

        return "admin/itemForm";
    }

    @PostMapping(value = "/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto,
                          BindingResult bindingResult,
                          Model model,
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
        }
        catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록중 에러가 발생하였습니다.");
            return "admin/itemForm";
        }

        return "redirect:/admin/items";
    }

    //상품관리
    @GetMapping(value = {"/items", "/items/{page}"}) //상품관리리스트, 상품 수정페이지
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("maxPage", 3);

        return "admin/items";
    }

//    //상품삭제
//    @DeleteMapping(value="/items/delete")
//    public @ResponseBody ResponseEntity deleteItem(@RequestBody ItemDeleteDto itemDeleteDto){
//        List<ItemDeleteDto> itemDeleteDtoList = itemDeleteDto.getItemDeleteDtoList();
//
//        if(itemDeleteDtoList==null || itemDeleteDtoList.size() ==0 ){
//            return new ResponseEntity<String>("삭제할 상품을 선택하세요", HttpStatus.FORBIDDEN);
//        }
//
//        Long deleteId= itemService.deleteItem(itemDeleteDtoList);
//
//        return new ResponseEntity<Long>(deleteId, HttpStatus.OK);
//    }

    //상품 수정
    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(@PathVariable("itemId")Long itemId, Model model){

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

    @PostMapping(value = "/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto,
                             BindingResult bindingResult,
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
        }
        catch(Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "admin/itemForm";
        }

        return "redirect:/admin/items";
    }

    //상품 삭제
    //삭제기능 구현은 했지만 img와 외래키로 연결되어있어 작성하는 글 삭제 하지않고 구분자만 지정하여 구분자를 Delete로 두고 hide 시킬것
    @PutMapping(value = "/item/hidden")
    public @ResponseBody ResponseEntity modifyDelItem(@RequestBody ItemDeleteDto itemDeleteDto){

        List<ItemDeleteDto> itemDeleteDtoList = itemDeleteDto.getItemDeleteDtoList();

        if(itemDeleteDtoList == null || itemDeleteDtoList.size()==0){
            return new ResponseEntity<String>("삭제할 상품을 선택하세요.", HttpStatus.FORBIDDEN);
        }

        Long itemId = itemService.modify(itemDeleteDto);


        return new ResponseEntity<Long>(itemId, HttpStatus.OK);

    }


    //전체주문내역
    @GetMapping(value = "/orders")
    public String allOders(){
        return "admin/orders";
    }

    //공지등록
    //공지사항 등록하기
    @GetMapping(value = "/notice/new")
    public String noticeForm(Model model){
        model.addAttribute("noticeFormDto", new NoticeFormDto());
        return "admin/noticeForm";
    }

    @PostMapping(value = "/notice/new")
    public String noticeNew(@Valid NoticeFormDto noticeFormDto,
                            BindingResult bindingResult,
                            Model model,
                            @RequestParam("noticeImgFile") List<MultipartFile> noticeImgFileList){
        if(bindingResult.hasErrors()){
            return "admin/noticeForm";
        }

        try{
            noticeService.saveNotice(noticeFormDto, noticeImgFileList);
        }
        catch (Exception e){
            model.addAttribute("errorMessage", "게시글 등록중 에러가 발생했습니다.");
            return "admin/noticeForm";
        }

        return "redirect:/notice";
    }


    //공지사항 수정 : 내용
    @GetMapping(value = "/notice/{noticeId}")
    public String noticeUpdate(@PathVariable("noticeId")Long noticeId, Model model){

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

    // 공지사항 수정 : 이미지
    @PostMapping(value = "/notice/{noticeId}")
    public String noticeUpdate(@Valid NoticeFormDto noticeFormDto,
                               BindingResult bindingResult,
                               @RequestParam("noticeImgFile") List<MultipartFile> noticeImgFileList,
                               Model model){

        if(bindingResult.hasErrors()){
            return "admin/noticeForm";
        }

        if(noticeImgFileList.get(0).isEmpty() && noticeFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "admin/noticeForm";
        }

        try{
            noticeService.updateNotice(noticeFormDto, noticeImgFileList);
        }
        catch(Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "admin/noticeForm";
        }

        return "redirect:/notice";
    }

    //공지사항 삭제
    //삭제기능 구현은 했지만 img와 외래키로 연결되어있어 작성하는 글 삭제 하지않고 구분자만 지정하여 구분자를 Delete로 두고 hide 시킬것
    @PutMapping(value = "/notice/hidden")
    public @ResponseBody ResponseEntity modifyNotice(@RequestBody NoticeDeleteDto noticeDeleteDto){

        List<NoticeDeleteDto> noticeDeleteDtoList = noticeDeleteDto.getNoticeDeleteDtoList();

        if(noticeDeleteDtoList == null || noticeDeleteDtoList.size()==0){
            if(noticeDeleteDtoList == null){
            }
            return new ResponseEntity<String>("삭제할 게시글을 선택하세요.", HttpStatus.FORBIDDEN);
        }

        Long noticeId = noticeService.modify(noticeDeleteDto);


        return new ResponseEntity<Long>(noticeId, HttpStatus.OK);

    }


    //회원관리
    @GetMapping(value = "/members" )
    public String memberList(){
        return "admin/members";
    }
}

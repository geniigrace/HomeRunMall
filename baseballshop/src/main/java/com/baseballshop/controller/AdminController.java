package com.baseballshop.controller;

import com.baseballshop.dto.ItemFormDto;
import com.baseballshop.dto.ItemSearchDto;
import com.baseballshop.entity.Item;
import com.baseballshop.repository.ItemRepository;
import com.baseballshop.service.ItemService;
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

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.baseballshop.entity.QItem.item;

@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;

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
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model, ItemFormDto itemFormDto){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);

        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);

        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("itemFormDto", itemFormDto);
        model.addAttribute("maxPage", 3);

        return "admin/items";
    }

    //상품삭제

    //전체주문내역
    @GetMapping(value = "/orders")
    public String allOders(){
        return "admin/orders";
    }

    //공지등록
    @GetMapping(value = "/notice/new")
    public String noticeForm(){
        return "admin/noticeForm";
    }

    //회원관리
    @GetMapping(value = "/members" )
    public String memberList(){
        return "admin/members";
    }
}

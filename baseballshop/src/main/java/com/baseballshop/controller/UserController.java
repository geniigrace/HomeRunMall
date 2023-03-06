package com.baseballshop.controller;

import com.baseballshop.dto.*;
import com.baseballshop.entity.Member;
import com.baseballshop.repository.MemberRepository;
import com.baseballshop.service.CartService;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequestMapping("/user")
@Controller
@RequiredArgsConstructor
public class UserController {

    private final CartService cartService;
    private final OrderService orderService;
    private final MemberRepository memberRepository;
    private final HttpSession httpSession;
    //마이페이지
    @GetMapping(value = "/mypage")
    public String mypage(Model model, Principal principal){

        if(principal!=null){
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                SessionUser user = (SessionUser)httpSession.getAttribute("member");
                model.addAttribute("loginName", user.getName());

            } else {
                model.addAttribute("loginName", member.getName());
            }
        }

        return "user/mypage";
    }
    //장바구니 페이지 생성 및 연결
    @GetMapping(value = "/cart")
    public String orderHist(Principal principal, Model model){

        List<CartDto> cartDtoList;
        String email="";

        if(principal!=null) { //로그인 했을 때
            Member member = memberRepository.findByEmail(principal.getName());

            if (member == null) {//소셜 로그인 일 때
                SessionUser user = (SessionUser) httpSession.getAttribute("member");

                //사용자 이름
                model.addAttribute("loginName", user.getName());

                //카트로 연결
                email = user.getEmail();
                cartDtoList = cartService.getCartList(email);

            } else {//로컬 로그인 일 때
                //사용자 이름
                model.addAttribute("loginName", member.getName());

                //카트로 연결
                email = principal.getName();
                cartDtoList = cartService.getCartList(email);
            }
            model.addAttribute("cartItems", cartDtoList);
        }
            return "user/cart";
    }

    //장바구니에 상품 담기
    @PostMapping(value = "/cart")
    public @ResponseBody ResponseEntity addCart (@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email="";
        Long cartItemId;

        try{
            if(principal!=null) { //로그인 했을 때
                Member member = memberRepository.findByEmail(principal.getName());

                if (member == null) {//소셜 로그인 일 때
                    SessionUser user = (SessionUser)httpSession.getAttribute("member");
                    email = user.getEmail();
                    cartItemId = cartService.addCart(cartItemDto, email);

                } else {//로컬 로그인 일 때
                    email = principal.getName();
                    cartItemId = cartService.addCart(cartItemDto, email);
                }
            }
            else {
                return new ResponseEntity<String>("로그인이 필요합니다.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    //장바구니 수량 변경
    @PatchMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId, int count, Principal principal){

        String email ="";
        Member member = memberRepository.findByEmail(principal.getName());

        if(member == null ){ //소셜 로그인 일 때
            SessionUser user = (SessionUser)httpSession.getAttribute("member");
            email = user.getEmail();
        }
        else{
            email=principal.getName();
        }

        if(count <=0){
            return new ResponseEntity<String>("최소 1개 이상 담아주세요.", HttpStatus.BAD_REQUEST);
        }
        else if( !cartService.validateCartItem(cartItemId, email)){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.updateCartItemCount(cartItemId, count);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    //장바구니 상품 삭제
    @DeleteMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal){
        String email="";
        Member member = memberRepository.findByEmail(principal.getName());

        if(member == null ){ //소셜 로그인 일 때
            SessionUser user = (SessionUser)httpSession.getAttribute("member");
            email = user.getEmail();
        }
        else{
            email=principal.getName();
        }

        if (!cartService.validateCartItem(cartItemId, email)) {
            return new ResponseEntity<String>("수정권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    //주문
    @GetMapping(value = {"/orderlist","/orderlist/{page}"})

    public String order(Model model, Principal principal, @PathVariable("page") Optional<Integer> page){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        String email = "";

        if(principal!=null) {//로그인 했을 때
            Member member = memberRepository.findByEmail(principal.getName());

            if (member == null) {//소셜로그인 일 때
                SessionUser user = (SessionUser) httpSession.getAttribute("member");
                email=user.getEmail();
                model.addAttribute("loginName", user.getName());

            } else {//로컬 로그인 일 때
                email= principal.getName();
                model.addAttribute("loginName", member.getName());
            }

            Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(email, pageable);

            model.addAttribute("orders", orderHistDtoList);
            model.addAttribute("page", pageable.getPageNumber());
            model.addAttribute("maxPage", 5);
        }
            return "user/orderlist";
    }

    //장바구니에서 주문
    @PostMapping(value = "/cart/order")
    public @ResponseBody ResponseEntity orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal){

        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

        String email="";
        Member member = memberRepository.findByEmail(principal.getName());

        if(member == null ){ //소셜 로그인 일 때
            SessionUser user = (SessionUser)httpSession.getAttribute("member");
            email = user.getEmail();
        }
        else{
            email=principal.getName();
        }

        if(cartOrderDtoList == null || cartOrderDtoList.size() ==0){
            return new ResponseEntity<String>("주문할 상품을 선택해주세요.", HttpStatus.FORBIDDEN);
        }
        for(CartOrderDto cartOder : cartOrderDtoList){
            if (!cartService.validateCartItem(cartOder.getCartItemId(), email)) {
                return new ResponseEntity<String>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        }
        Long orderId = cartService.orderCartItem(cartOrderDtoList, email);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    //제품 상세페이지에서 주문
    @PostMapping(value = "/itemdtl/order")
    public @ResponseBody
    ResponseEntity order(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, Principal principal){

        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for(FieldError fieldError : fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }

            return new ResponseEntity<String> (sb.toString(),HttpStatus.BAD_REQUEST);
        }

        String email="";
        Long orderId;

        if(principal!=null) { //로그인 되어 있을 때
            Member member = memberRepository.findByEmail(principal.getName());
            if(member==null){ //소셜 로그인 일 때
                SessionUser user = (SessionUser) httpSession.getAttribute("member");
                email = user.getEmail();
            }
            else {//로컬 로그인 일 때
                email=principal.getName();
            }

            try {
                orderId = orderService.order(orderDto, email);
            } catch (Exception e) {
                return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<Long>(orderId, HttpStatus.OK);
        }
        else {//로그인 없이 접근시
            return new ResponseEntity<String>("로그인이 필요합니다.", HttpStatus.FORBIDDEN);
        }
    }

    //찜하기
     @GetMapping(value = "/like")
    public String like(Model model, Principal principal){

        if(principal!=null){
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                SessionUser user = (SessionUser)httpSession.getAttribute("member");
                model.addAttribute("loginName", user.getName());

            } else {
                model.addAttribute("loginName", member.getName());
            }
        }

        return "user/like";
    }

    //회원정보 조회
    @GetMapping(value = "/myinfo")
    public String myinfo(Model model, Principal principal){

        if(principal!=null){
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                SessionUser user = (SessionUser)httpSession.getAttribute("member");
                model.addAttribute("loginName", user.getName());

            } else {
                model.addAttribute("loginName", member.getName());
            }
        }

        return "user/myinfo";
    }

    //회원정보 수정 : 회원가입 페이지에서 읽어오고 아이디만 수정할수 없게 처리
    @GetMapping(value = "/myinfoEdit")
    public String myinfoEdit(Model model, Principal principal){

        if(principal!=null){
            Member member = memberRepository.findByEmail(principal.getName());
            if (member == null) {
                SessionUser user = (SessionUser)httpSession.getAttribute("member");
                model.addAttribute("loginName", user.getName());

            } else {
                model.addAttribute("loginName", member.getName());
            }
        }

        return "user/myinfoEdit";
    }
}

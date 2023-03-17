package com.baseballshop.controller;

import com.baseballshop.dto.*;
import com.baseballshop.service.CartService;
import com.baseballshop.service.LoginUserService;
import com.baseballshop.service.MemberService;
import com.baseballshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequestMapping("/user")
@Controller
@RequiredArgsConstructor
public class UserController {

    private final CartService cartService;
    private final OrderService orderService;
    private final MemberService memberService;
    private final LoginUserService loginUserService;
    private final PasswordEncoder passwordEncoder;


    //마이페이지
    @GetMapping(value = "/mypage")
    public String mypage(Model model, Principal principal){

        if(principal!=null){
            String loginName = loginUserService.loginUserNameEmail(principal)[0];
            model.addAttribute("loginName", loginName);
            return "user/mypage";
        }
        else {
            return "member/memberLoginForm";
        }

    }

    //장바구니 페이지 생성 및 연결
    @GetMapping(value = "/cart")
    public String orderHist(Principal principal, Model model){

        List<CartDto> cartDtoList;

        if(principal!=null) { //로그인 했을 때
            String loginName = loginUserService.loginUserNameEmail(principal)[0];
            model.addAttribute("loginName", loginName);

            String loginEmail = loginUserService.loginUserNameEmail(principal)[1];
            cartDtoList = cartService.getCartList(loginEmail);
            model.addAttribute("cartItems", cartDtoList);

            return "user/cart";
        }
        else {
            return "member/memberLoginForm";
        }
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

        Long cartItemId;

        try{
            if(principal!=null) { //로그인 했을 때
                String loginEmail = loginUserService.loginUserNameEmail(principal)[1];
                cartItemId = cartService.addCart(cartItemDto, loginEmail);

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

        String loginEmail = loginUserService.loginUserNameEmail(principal)[1];

        if(count <=0){
            return new ResponseEntity<String>("최소 1개 이상 담아주세요.", HttpStatus.BAD_REQUEST);
        }
        else if( !cartService.validateCartItem(cartItemId, loginEmail)){
            return new ResponseEntity<String>("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        cartService.updateCartItemCount(cartItemId, count);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    //장바구니 상품 삭제
    @DeleteMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleteCartItem(@PathVariable("cartItemId") Long cartItemId, Principal principal){
        String loginEmail = loginUserService.loginUserNameEmail(principal)[1];

        if (!cartService.validateCartItem(cartItemId, loginEmail)) {
            return new ResponseEntity<String>("수정권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        cartService.deleteCartItem(cartItemId);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    //주문
    @GetMapping(value = {"/orderlist","/orderlist/{page}"})

    public String order(Model model, Principal principal, @PathVariable("page") Optional<Integer> page){

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

        if(principal!=null) {//로그인 했을 때
            String loginName = loginUserService.loginUserNameEmail(principal)[0];
            model.addAttribute("loginName", loginName);

            String loginEmail = loginUserService.loginUserNameEmail(principal)[1];

            Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(loginEmail, pageable);

            model.addAttribute("orders", orderHistDtoList);
            model.addAttribute("maxPage", 10);

            return "user/orderlist";
        }
        else {
            return "member/memberLoginForm";
        }
    }

    //장바구니에서 주문
    @PostMapping(value = "/cart/order")
    public @ResponseBody ResponseEntity orderCartItem(@RequestBody CartOrderDto cartOrderDto, Principal principal){

        List<CartOrderDto> cartOrderDtoList = cartOrderDto.getCartOrderDtoList();

        String loginEmail = loginUserService.loginUserNameEmail(principal)[1];

        if(cartOrderDtoList == null || cartOrderDtoList.size() ==0){
            return new ResponseEntity<String>("주문할 상품을 선택해주세요.", HttpStatus.FORBIDDEN);
        }
        for(CartOrderDto cartOder : cartOrderDtoList){
            if (!cartService.validateCartItem(cartOder.getCartItemId(), loginEmail)) {
                return new ResponseEntity<String>("주문 권한이 없습니다.", HttpStatus.FORBIDDEN);
            }
        }
        Long orderId = cartService.orderCartItem(cartOrderDtoList, loginEmail);
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

        Long orderId;

        if(principal!=null) { //로그인 되어 있을 때
            String loginEmail = loginUserService.loginUserNameEmail(principal)[1];
            try {
                orderId = orderService.order(orderDto, loginEmail);
            } catch (Exception e) {
                return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<Long>(orderId, HttpStatus.OK);
        }
        else {//로그인 없이 접근시
            return new ResponseEntity<String>("로그인이 필요합니다.", HttpStatus.FORBIDDEN);
        }
    }

    //주문취소
    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal){

        String loginEmail = loginUserService.loginUserNameEmail(principal)[1];

        if(!orderService.validateOrder(orderId, loginEmail)){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    //회원정보 수정
    @GetMapping(value = "/myinfo")
    public String myinfo(Model model, Principal principal){


        if(principal!=null){
            String loginName = loginUserService.loginUserNameEmail(principal)[0];
            model.addAttribute("loginName", loginName);

            String loginEmail = loginUserService.loginUserNameEmail(principal)[1];
            MemberModifyDto memberModifyDto = memberService.getMemberInfo(loginEmail);

            Boolean loginRoot= loginUserService.loginUserType(principal);
            memberModifyDto.setLoginRoot(loginRoot);
            model.addAttribute("memberModifyDto",memberModifyDto);

            return "user/myinfo";
        }
        else {
            return "member/memberLoginForm";
        }
    }

    @PostMapping(value = "/myinfo")
    public String myinfoModify(MemberModifyDto memberModifyDto, BindingResult bindingResult, Model model,Principal principal, HttpServletResponse response) {

        if (bindingResult.hasErrors()) {
            return "user/myinfo";
        }

        try {
            if(memberModifyDto.getPassword() != null) {
                memberModifyDto.setPassword(passwordEncoder.encode(memberModifyDto.getPassword()));
            }
            memberService.updateMember(memberModifyDto, memberModifyDto.getEmail());

            response.setContentType("text/html; charset=euc-kr");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('회원정보가 수정되었습니다.'); location.href='/user/myinfo'; </script>");
            out.flush();

        }  catch (Exception e){
            model.addAttribute("errorMessage", "회원정보 수정 중 에러가 발생하였습니다.");
            return "user/myinfo";
        }

        return "redirect:/user/myinfo";

    }
}

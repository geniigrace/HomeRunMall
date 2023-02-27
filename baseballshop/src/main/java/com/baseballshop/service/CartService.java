package com.baseballshop.service;

import com.baseballshop.dto.CartDto;
import com.baseballshop.dto.CartItemDto;
import com.baseballshop.entity.Cart;
import com.baseballshop.entity.CartItem;
import com.baseballshop.entity.Item;
import com.baseballshop.entity.Member;
import com.baseballshop.repository.CartItemRepository;
import com.baseballshop.repository.CartRepository;
import com.baseballshop.repository.ItemRepository;
import com.baseballshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;


    public Long addCart(CartItemDto cartItemDto, String email){
        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityExistsException::new);

        Member member = memberRepository.findByEmail(email);

        Cart cart = cartRepository.findByMemberId(member.getId());

        if (cart == null) {
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());
        if (savedCartItem != null) {
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }

    @Transactional(readOnly=true)
    public List<CartDto> getCartList(String email){

        List<CartDto> cartDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);

        Cart cart = cartRepository.findByMemberId(member.getId());

        if(cart == null){
            return cartDtoList;
        }

        cartDtoList = cartItemRepository.findCartDtoList(cart.getId());

        return cartDtoList;
    }
}

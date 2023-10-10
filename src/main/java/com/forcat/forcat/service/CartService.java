package com.forcat.forcat.service;

import com.forcat.forcat.dto.shop.CartDetailDto;
import com.forcat.forcat.dto.shop.CartItemDto;
import com.forcat.forcat.entity.Cart;
import com.forcat.forcat.entity.CartItem;
import com.forcat.forcat.entity.Item;
import com.forcat.forcat.entity.Member;
import com.forcat.forcat.repository.CartItemRepository;
import com.forcat.forcat.repository.CartRepository;
import com.forcat.forcat.repository.ItemRepository;
import com.forcat.forcat.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Long addCart(CartItemDto cartItemDto, String email){

        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        Member member = memberOptional.orElseThrow(EntityNotFoundException::new);
        Cart cart = cartRepository.findByMember_Email(member.getEmail());
        if(cart == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if(savedCartItem != null){
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email){

        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Optional<Member> memberOptional = memberRepository.findByEmail(email);
        Member member = memberOptional.orElseThrow(EntityNotFoundException::new);

        Cart cart = cartRepository.findByMember_Email(member.getEmail());
        if(cart == null){
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());
        return cartDetailDtoList;
    }
}
package com.forcat.forcat.service;

import com.forcat.forcat.dto.shop.CartDetailDto;
import com.forcat.forcat.dto.shop.CartItemDto;
import com.forcat.forcat.dto.shop.CartOrderDto;
import com.forcat.forcat.dto.shop.OrderDto;
import com.forcat.forcat.entity.Cart;
import com.forcat.forcat.entity.CartItem;
import com.forcat.forcat.entity.Item;
import com.forcat.forcat.entity.Member;
import com.forcat.forcat.repository.CartItemRepository;
import com.forcat.forcat.repository.CartRepository;
import com.forcat.forcat.repository.ItemRepository;
import com.forcat.forcat.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

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
    private final OrderService orderService;

    public Long addCart (CartItemDto cartItemDto, String mid) {
        Item item = itemRepository.findById (cartItemDto.getItemId ()).orElseThrow (EntityNotFoundException::new);
        Optional<Member> memberOptional = memberRepository.findByMid (mid);
        Member member = memberOptional.orElseThrow (EntityNotFoundException::new);
        Cart cart = cartRepository.findByMember_Mid (member.getMid ());
        if (cart == null) {
            cart = Cart.createCart (member);
            cartRepository.save (cart);
        }
        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId (cart.getId (), item.getId ());
        if (savedCartItem != null) {
            savedCartItem.addCount (cartItemDto.getCount ());
            return savedCartItem.getId ();
        } else {
            CartItem cartItem = CartItem.createCartItem (cart, item, cartItemDto.getCount ());
            cartItemRepository.save (cartItem);
            return cartItem.getId ();
        }
    }

    @Transactional (readOnly = true)
    public List<CartDetailDto> getCartList (String mid) {
        List<CartDetailDto> cartDetailDtoList = new ArrayList<> ();
        Optional<Member> memberOptional = memberRepository.findByMid (mid);
        Member member = memberOptional.orElseThrow (EntityNotFoundException::new);
        Cart cart = cartRepository.findByMember_Mid (member.getMid ());
        if (cart == null) {
            return cartDetailDtoList;
        }
        cartDetailDtoList = cartItemRepository.findCartDetailDtoList (cart.getId ());
        return cartDetailDtoList;
    }

    @Transactional (readOnly = true)
    public boolean validateCartItem (Long cartItemId, String mid) {
        Optional<Member> memberOptional = memberRepository.findByMid (mid);
        Member curMember = memberOptional.orElseThrow (EntityNotFoundException::new);
        CartItem cartItem = cartItemRepository.findById (cartItemId).orElseThrow (EntityNotFoundException::new);
        Member savedMember = cartItem.getCart ().getMember ();
        return StringUtils.equals (curMember.getMid (), savedMember.getMid ());
    }

    public void updateCartItemCount (Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findById (cartItemId).orElseThrow (EntityNotFoundException::new);
        cartItem.updateCount (count);
    }

    public void deleteCartItem (Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById (cartItemId).orElseThrow (EntityNotFoundException::new);
        cartItemRepository.delete (cartItem);
    }

    public Long orderCartItem (List<CartOrderDto> cartOrderDtoList, String mid) {
        List<OrderDto> orderDtoList = new ArrayList<> ();
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById (cartOrderDto.getCartItemId ()).orElseThrow (EntityNotFoundException::new);
            OrderDto orderDto = new OrderDto ();
            orderDto.setItemId (cartItem.getItem ().getId ());
            orderDto.setCount (cartItem.getCount ());
            orderDtoList.add (orderDto);
        }
        Long orderId = orderService.orders (orderDtoList, mid);
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            CartItem cartItem = cartItemRepository.findById (cartOrderDto.getCartItemId ()).orElseThrow (EntityNotFoundException::new);
            cartItemRepository.delete (cartItem);
        }
        return orderId;
    }
}
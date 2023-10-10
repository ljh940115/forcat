package com.forcat.forcat.service;

import com.forcat.forcat.entity.ItemSellStatus;
import com.forcat.forcat.dto.shop.CartItemDto;
import com.forcat.forcat.entity.CartItem;
import com.forcat.forcat.entity.Item;
import com.forcat.forcat.entity.Member;
import com.forcat.forcat.repository.CartItemRepository;
import com.forcat.forcat.repository.ItemRepository;
import com.forcat.forcat.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class CartServiceTest {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CartService cartService;
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public Item saveItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public Member saveMember(){
        Member member = new Member();
        member.setMid("ggggg");
        member.setName("John Doe");
        member.setEmail("test@test.com");
        member.setAddress("123 Main St");
        member.setMpw(passwordEncoder.encode("password123"));
        return memberRepository.save(member);

    }

    @Test
    @DisplayName("장바구니 담기 테스트")
    public void addCart(){
        Item item = saveItem();
        Member member = saveMember();

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setCount(5);
        cartItemDto.setItemId(item.getId());

        Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(item.getId(), cartItem.getItem().getId());
        assertEquals(cartItemDto.getCount(), cartItem.getCount());
    }

}
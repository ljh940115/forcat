package com.forcat.forcat.repository;

import com.forcat.forcat.dto.shop.CartDetailDto;
import com.forcat.forcat.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndItemId (Long cartId, Long itemId);

    @Query ("select new com.forcat.forcat.dto.shop.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " + "from CartItem ci, ItemImg im " + "join ci.item i " + "where ci.cart.id = :cartId " + "and im.item.id = ci.item.id " + "and im.repImgYn = 'Y' " + "order by ci.regDate desc")
    List<CartDetailDto> findCartDetailDtoList (@Param ("cartId") Long cartId);
}
package com.forcat.forcat.dto.shop;

import com.forcat.forcat.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {//주문 상품 정보 : 주문 상품과 이미지를 가져온다.

    private String itemNm; //상품명
    private int count; //주문 수량
    private int orderPrice; //주문 금액
    private String imgUrl; //상품 이미지 경로
    public OrderItemDto (OrderItem orderItem, String imgUrl) {
        this.itemNm = orderItem.getItem ().getItemNm ();
        this.count = orderItem.getCount ();
        this.orderPrice = orderItem.getOrderPrice ();
        this.imgUrl = imgUrl;
    }
}
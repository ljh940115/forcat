package com.forcat.forcat.dto.shop;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDetailDto {    //장바구니 조회

    private Long cartItemId; //장바구니 상품 아이디
    private String itemNm; //상품명
    private int price; //상품 금액
    private int count; //수량
    private String imgUrl; //상품 이미지 경로

    //장바구니 페이지에 전달할 리스트를 쿼리 하나로 조회하는 JPQL
    public CartDetailDto (Long cartItemId, String itemNm, int price, int count, String imgUrl) {
        this.cartItemId = cartItemId;
        this.itemNm = itemNm;
        this.price = price;
        this.count = count;
        this.imgUrl = imgUrl;
    }
}
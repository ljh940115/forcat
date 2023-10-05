package com.forcat.forcat.dto;

import com.forcat.forcat.entity.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemSearchDto { // 상품 조회 조건
    private String searchDateType;
    /*
    all : 상품 등록일 전체
    1d : 최근 하루 동안 등록된 상품
    1w : 최근 일주일 동안 등록된 상품
    1m : 최근 한 달 동안 등록된 상품
    6m : 최근 6개월 동안 등록된 상품
    */
    private ItemSellStatus searchSellStatus; // 상품 판매 상태 기준
    private String searchBy; // 상품 조회 유형 (itemNm : 상품명 / createdBy : 상품 등록자 아이디)
    private String searchQuery = ""; // 조회 검색어 저장 변수
}

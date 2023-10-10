package com.forcat.forcat.dto.shop;

import lombok.Data;

@Data
public class ItemDto {

    private Long id;
    private String itemNm;
    private Integer price;
    private String itemDetail;
    private String sellStatCd;
}

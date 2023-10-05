package com.forcat.forcat.dto;

import lombok.Data;

@Data
public class ItemDto {

    private Long id;
    private String itemNm;
    private Integer price;
    private String itemDetail;
    private String sellStatCd;
}

package com.forcat.forcat.dto.shop;

import com.forcat.forcat.entity.Item;
import com.forcat.forcat.entity.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {

    private static ModelMapper modelMapper = new ModelMapper ();

    private Long id;
    @NotBlank (message = "상품명은 필수 입력 값입니다.")
    private String itemNm;
    @NotNull (message = "가격은 필수 입력 값입니다.")
    private Integer price;
    @NotBlank (message = "이름은 필수 입력 값입니다.")
    private String itemDetail;
    @NotNull (message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;
    private ItemSellStatus itemSellStatus;
    // 상품 저장 후 수정 시 이미지 정보 저장 리스트
    private List<ItemImgDto> itemImgDtoList = new ArrayList<> ();
    // 상품 이미지 아이디 저장 리스트 -> 수정 시 이미지 아이디 담는 용도
    private List<Long> itemImgIds = new ArrayList<> ();

    // 모델 매퍼를 이용한 엔티티, DTO 매핑
    public static ItemFormDto of (Item item) { // Entity -> DTO
        return modelMapper.map (item, ItemFormDto.class);
    }

    // 모델 매퍼를 이용한 엔티티, DTO 매핑
    public Item createItem () { // DTO -> Entity
        return modelMapper.map (this, Item.class);
    }
}


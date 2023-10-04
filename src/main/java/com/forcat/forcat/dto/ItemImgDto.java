package com.forcat.forcat.dto;

import com.forcat.forcat.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto {
    private Long id;
    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repImgYn;
    private static ModelMapper modelMapper = new ModelMapper(); // ModelMapper 객체 추가

    public static ItemImgDto of(ItemImg itemImg) {
        // ModelMapper를 사용하여 엔티티를 DTO로 매핑하고, 매핑된 DTO 객체를 반환
        return modelMapper.map(itemImg, ItemImgDto.class);
    }
}

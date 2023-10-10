package com.forcat.forcat.service;

import com.forcat.forcat.dto.shop.ItemFormDto;
import com.forcat.forcat.dto.shop.ItemImgDto;
import com.forcat.forcat.dto.shop.ItemSearchDto;
import com.forcat.forcat.dto.MainItemDto;
import com.forcat.forcat.entity.Item;
import com.forcat.forcat.entity.ItemImg;
import com.forcat.forcat.repository.ItemImgRepository;
import com.forcat.forcat.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        // 상품 등록
        Item item = itemFormDto.createItem(); // itemDTO를 entity로 변환하는 메소드 사용하여 item으로 저장
        itemRepository.save(item); // itemRepository를 이용하여 DB에 저장

        // 이미지 등록
        for(int i = 0; i < itemImgFileList.size(); i++) { // 상품 이미지 파일 목록 순회
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i == 0) // 첫 번째 이미지인 경우
                itemImg.setRepImgYn("Y"); // 대표이미지 여부를 Y로 설정
            else // 아니라면
                itemImg.setRepImgYn("N"); // 대표이미지 여부를 N으로 설정
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }
        return item.getId();
    }

    @Transactional(readOnly = true) // 데이터 수정 없으므로 최적화를 위해 readOnly 옵션 넣기
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    } // 상품 데이터 조회 메소드

    @Transactional(readOnly = true)//등록된 상품 불러오기
    public ItemFormDto getItemDtl(Long itemId){
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        //상품 수정
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);
        List<Long> itemImgIds = itemFormDto.getItemImgIds();

        //이미지 등록
        for(int i=0;i<itemImgFileList.size();i++){
            itemImgService.updateItemImg(itemImgIds.get(i),
                    itemImgFileList.get(i));
        }

        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }
}

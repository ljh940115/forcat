package com.forcat.forcat.service;

import com.forcat.forcat.dto.shop.ItemFormDto;
import com.forcat.forcat.entity.Item;
import com.forcat.forcat.entity.ItemImg;
import com.forcat.forcat.entity.ItemSellStatus;
import com.forcat.forcat.repository.ItemImgRepository;
import com.forcat.forcat.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemImgRepository itemImgRepository;

    List<MultipartFile> createMultipartFiles () throws Exception {
        // MultipartFile : HTTP 요청을 통해 전송한 파일 데이터를 처리 인터페이스
        List<MultipartFile> multipartFileList = new ArrayList<> ();
        for (int i = 0; i < 5; i++) {
            String path = "D:/shopForcat/item";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile = new MockMultipartFile (path, imageName, "image/jpg", new byte[]{1, 2, 3, 4});
            multipartFileList.add (multipartFile);
        }
        return multipartFileList;
    } // 테스트용 MultipartFile 리스트 생성 메소드

    @Test
    @DisplayName ("상품 등록 테스트")
    @WithMockUser (username = "admin", roles = "ADMIN")
    void saveItem () throws
                     Exception {
        ItemFormDto itemFormDto = new ItemFormDto ();
        itemFormDto.setItemNm ("테스트상품");
        itemFormDto.setItemSellStatus (ItemSellStatus.SELL);
        itemFormDto.setItemDetail ("테스트상품입니다.");
        itemFormDto.setPrice (1000);
        itemFormDto.setStockNumber (100);
        List<MultipartFile> multipartFileList = createMultipartFiles ();
        Long itemId = itemService.saveItem (itemFormDto, multipartFileList);
        // 상품 정보와 이미지를 저장후 상품의 id를 반환 받아 itemId로 저장
        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc (itemId);
        Item item = itemRepository.findById (itemId).orElseThrow (EntityNotFoundException::new);
        assertEquals (itemFormDto.getItemNm (), item.getItemNm ());
        assertEquals (itemFormDto.getItemSellStatus (), item.getItemSellStatus ());
        assertEquals (itemFormDto.getItemDetail (), item.getItemDetail ());
        assertEquals (itemFormDto.getPrice (), item.getPrice ());
        assertEquals (itemFormDto.getStockNumber (), item.getStockNumber ());
        assertEquals (multipartFileList.get (0).getOriginalFilename (), itemImgList.get (0).getOriImgName ());
        // assertEquals(A, B) : 두 객체의 값이 같은가?
    }
}

package com.forcat.forcat.service;

import com.forcat.forcat.entity.ItemImg;
import com.forcat.forcat.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
// 의존성 자동 주입 (private final, @NotNull)
// -> itemImgRepository, fileService만 생성자 생성 됨
@Transactional
public class ItemImgService {
    @Value("${itemImgLocation}") // application.properties에 등록한 프로퍼티 값 불러옴
    private String itemImgLocation;
    private final ItemImgRepository itemImgRepository;
    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename(); // 파일의 기존 파일명을 oriImgName에 저장
        String imgName = "";
        String imgUrl = "";

        // 파일 업로드
        if(!StringUtils.isEmpty(oriImgName)) {
            // StringUtils.isEmpty : Null이면 true를 반환 -> ! 논리 부정 연산자
            // 파일명이 있다면
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            // uploadFile 메소드를 통해 새로운 파일명을 리턴 받아 imgName에 저장
            imgUrl = "/view/" + imgName;
        }

        // 상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);
    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{
        if(!itemImgFile.isEmpty()){
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
                    .orElseThrow(EntityNotFoundException::new);

            //기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation+"/"+
                        savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;
            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
        }
    }
}


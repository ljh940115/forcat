package com.forcat.forcat.service;

import com.forcat.forcat.dto.content.*;
import com.forcat.forcat.entity.Content;

import java.util.List;
import java.util.stream.Collectors;

public interface ContentService {

    Long ContentRegister (ContentDTO dto);//게시글 등록

    ContentDTO ContentReadOne (Long cno);//게시글 조회

    void contentModify (ContentDTO contentDTO);//게시글 수정

    void contentRemove (Long cno);//게시글 삭제

    ContentPageResponseDTO<ContentDTO> contentList (ContentPageRequestDTO contentPageRequestDTO);//페이지 목록/검색

    ContentPageResponseDTO<ContentListReplyCountDTO> contentListWithReplyCount (ContentPageRequestDTO contentPageRequestDTO);//댓글의 숫자까지 처리

    ContentPageResponseDTO<ContentListAllDTO> contentListWithAll (ContentPageRequestDTO contentPageRequestDTO);//게시글의 이미지와 댓글의 숫자까지 처리

    ContentPageResponseDTO<ContentListAllDTO> searchWithContentRecent (ContentPageRequestDTO contentPageRequestDTO);

    default Content dtoToEntity (ContentDTO contentDTO) {//DTO 객체를 Entity로 변환
        Content content = Content.builder ().cno (contentDTO.getCno ()).ctitle (contentDTO.getCtitle ()).ccontent (contentDTO.getCcontent ()).cwriter (contentDTO.getCwriter ()).build ();
        if (contentDTO.getFileNames () != null) {//파일 이름 목록이 비어있는지 확인
            contentDTO.getFileNames ().forEach (fileName -> {//비어있지 않으면 파일 이름 분리, 엔티티에 이미지 정보 추가
                String[] arr = fileName.split ("_");
                content.addImage (arr[0], arr[1]);
            });
        }
        return content;
    }

    default ContentDTO entityToDTO (Content content) {//Entity 객체를 DTO로 변환
        ContentDTO contentDTO = ContentDTO.builder ().cno (content.getCno ()).ctitle (content.getCtitle ()).ccontent (content.getCcontent ()).cwriter (content.getCwriter ()).regDate (content.getRegDate ()).modDate (content.getModDate ()).build ();
        //getImageSet를 통해 게시물에 연결된 이미지 목록을 가져와 파일 이름 목록으로 변환
        List<String> fileNames = content.getImageSet ().stream ().sorted ().map (contentImage -> contentImage.getUuid () + "_" + contentImage.getFileName ()).collect (Collectors.toList ());
        contentDTO.setFileNames (fileNames);//fileNames 목록을 boardDTO에 설정
        return contentDTO;
    }
}

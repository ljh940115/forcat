package com.forcat.forcat.service;

import com.forcat.forcat.dto.*;
import com.forcat.forcat.dto.notice.*;
import com.forcat.forcat.entity.Content;
import com.forcat.forcat.entity.Notice;

import java.util.List;
import java.util.stream.Collectors;

public interface NoticeService {

    Long NoticeRegister(NoticeDTO dto);

    // 게시글 조회
    NoticeDTO NoticeReadOne(Long noticeNo);

    void NoticeModify(NoticeDTO noticeDTO);

    // 게시글 삭제
    void NoticeRemove(Long noticeNo);

    // 페이지 목록/검색
    NoticePageResponseDTO<NoticeDTO> noticeList(NoticePageRequestDTO noticePageRequestDTO);

    // 댓글의 숫자까지 처리
    NoticePageResponseDTO<NoticeListReplyCountDTO> noticeListWithReplyCount(NoticePageRequestDTO noticePageRequestDTO);

    //게시글의 이미지와 댓글의 숫자까지 처리
    NoticePageResponseDTO<NoticeListAllDTO> noticeListWithAll(NoticePageRequestDTO noticePageRequestDTO);

    default Notice dtoToEntity(NoticeDTO noticeDTO){//DTO 객체를 Entity로 변환

        Notice notice = Notice.builder()
                .noticeNo(noticeDTO.getNoticeNo())
                .noticeTitle(noticeDTO.getNoticeTitle())
                .noticeContent(noticeDTO.getNoticeContent())
                .noticeWriter(noticeDTO.getNoticeWriter())
                .build();

        if(noticeDTO.getFileNames() != null){//파일 이름 목록이 비어있는지 확인

            noticeDTO.getFileNames().forEach(fileName -> {//비어있지 않으면 파일 이름 분리, 엔티티에 이미지 정보 추가
                String[] arr = fileName.split("_");
                notice.addImage(arr[0], arr[1]);
            });

        }

        return notice;
    }

    default NoticeDTO entityToDTO(Notice notice) {//Entity 객체를 DTO로 변환

        NoticeDTO noticeDTO = NoticeDTO.builder()
                .noticeNo(notice.getNoticeNo())
                .noticeTitle(notice.getNoticeTitle())
                .noticeContent(notice.getNoticeContent())
                .noticeWriter(notice.getNoticeWriter())
                .regDate(notice.getRegDate())
                .modDate(notice.getModDate())
                .build();

        //getImageSet를 통해 게시물에 연결된 이미지 목록을 가져와 파일 이름 목록으로 변환
        List<String> fileNames =
                notice.getImageSet().stream().sorted().map(noticeImage ->
                        noticeImage.getUuid()+"_"+noticeImage.getFileName()).collect(Collectors.toList());

        noticeDTO.setFileNames(fileNames);//fileNames 목록을 boardDTO에 설정

        return noticeDTO;
    }

}
package com.forcat.forcat.service;

import com.forcat.forcat.dto.*;
import com.forcat.forcat.dto.notice.*;
import com.forcat.forcat.entity.Content;
import com.forcat.forcat.entity.Notice;
import com.forcat.forcat.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service //서비스 명시
@RequiredArgsConstructor //상수 생성자 생성
@Log4j2 //로그 사용 명시
@Transactional//트랜잭션 처리
public class NoticeServiceImpl implements NoticeService {

    //데이터 변환용
    private final ModelMapper modelMapper;

    //DB 연결용
    private final NoticeRepository noticeRepository;

    //게시글 등록 구현
    //클라이언트로부터 전달된 게시글 정보 포함
    @Override
    public Long NoticeRegister(NoticeDTO noticeDTO) {

        Notice notice = dtoToEntity(noticeDTO);
        Long noticeNo = noticeRepository.save(notice).getNoticeNo();

        return noticeNo;
    }

    @Override
    public NoticeDTO NoticeReadOne(Long noticeNo) {

        Optional<Notice> result = noticeRepository.findByIdWithNoticeImages(noticeNo);

        Notice notice = result.orElseThrow();

        // BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);//엔티티 객체를 DTO로 변환
        NoticeDTO noticeDTO = entityToDTO(notice);

        // DTO 반환
        return noticeDTO;
    }

    @Override
    public void NoticeModify(NoticeDTO noticeDTO) {

        Optional<Notice> result = noticeRepository.findById(noticeDTO.getNoticeNo());
        Notice notice = result.orElseThrow();
        notice.change(noticeDTO.getNoticeTitle(), noticeDTO.getNoticeContent());

        //첨부파일의 처리
        notice.clearImages();

        if (noticeDTO.getFileNames() != null) {

            for (String fileName : noticeDTO.getFileNames()) {
                String[] arr = fileName.split("_");
                notice.addImage(arr[0], arr[1]);
            }

        }

        noticeRepository.save(notice);
    }

    @Override
    public void NoticeRemove(Long noticeNo) { noticeRepository.deleteById(noticeNo); }

    @Override
    public NoticePageResponseDTO<NoticeDTO> noticeList(NoticePageRequestDTO noticePageRequestDTO) {

        String[] types = noticePageRequestDTO.getTypes();
        String keyword = noticePageRequestDTO.getKeyword();
        Pageable Pageable = noticePageRequestDTO.getNoticePageable("noticeNo");

        //searchAll() 호출하여 게시글 검색, 검색 조건, 키워드 바탕으로 검색하고 페이지네이션 적용한 결과를 result 반환
        Page<Notice> result = noticeRepository.searchNoticeAll(types, keyword, Pageable);

        // 검색 결과인 Page<Board>를 BoardDTO로 변환하여 dtoList에 저장합니다.
        List<NoticeDTO> dtoNoticeList = result.getContent().stream()
                .map(notice -> modelMapper.map(notice, NoticeDTO.class)).collect(Collectors.toList());

        // PageResponseDTO 객체를 생성하고, 페이지 관련 정보와 변환된 게시글 목록, 전체 게시글 수를 설정합니다.
        // 이때, withAll() 메서드를 사용하여 기본 정보를 설정하고,
        // pageRequestDTO, dtoList, total 값을 설정한 뒤 build()로 최종 객체를 생성합니다.
        return NoticePageResponseDTO.<NoticeDTO>withNoticeAll()
                .noticePageRequestDTO(noticePageRequestDTO)
                .dtoNoticeList(dtoNoticeList)
                .noticeTotal((int) result.getTotalElements())
                .build();
    }

    @Override
    public NoticePageResponseDTO<NoticeListReplyCountDTO> noticeListWithReplyCount(NoticePageRequestDTO noticePageRequestDTO) {

        String[] types = noticePageRequestDTO.getTypes();//검색 조건
        String keyword = noticePageRequestDTO.getKeyword();//검색어
        Pageable pageable = noticePageRequestDTO.getNoticePageable("noticeNo");//페이지 번호, 크기, 정렬 정보

        // result값을 사용하여 PageResponseDTO 객체 생성
        Page<NoticeListReplyCountDTO> result = noticeRepository.searchWithNoticeReplyCount(types, keyword, pageable);

        return NoticePageResponseDTO.<NoticeListReplyCountDTO>withNoticeAll()//빌더 패턴 사용
                .noticePageRequestDTO(noticePageRequestDTO)//페이지 요청 정보 설정
                .dtoNoticeList(result.getContent())//BoardListReplyCountDTO 객체의 리스트 설정
                .noticeTotal((int) result.getTotalElements())//전체 결과 수 설정
                .build();
    }

    @Override
    public NoticePageResponseDTO<NoticeListAllDTO> noticeListWithAll(NoticePageRequestDTO noticePageRequestDTO) {

        String[] types = noticePageRequestDTO.getTypes();
        String keyword = noticePageRequestDTO.getKeyword();
        Pageable pageable = noticePageRequestDTO.getNoticePageable("noticeNo");

        Page<NoticeListAllDTO> result = noticeRepository.searchWithNoticeAll(types, keyword, pageable);

        return NoticePageResponseDTO.<NoticeListAllDTO>withNoticeAll()
                .noticePageRequestDTO(noticePageRequestDTO)
                .dtoNoticeList(result.getContent())
                .noticeTotal((int) result.getTotalElements())
                .build();
    }


}

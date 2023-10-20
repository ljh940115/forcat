package com.forcat.forcat.service;

import com.forcat.forcat.dto.content.*;
import com.forcat.forcat.dto.content.recent.ContentRPageRequestDTO;
import com.forcat.forcat.dto.content.recent.ContentRPageResponseDTO;
import com.forcat.forcat.entity.Content;
import com.forcat.forcat.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service //서비스 명시
@RequiredArgsConstructor //상수 생성자 생성
@Log4j2 //로그 사용 명시
@Transactional//트랜잭션 처리
public class ContentServiceImpl implements ContentService {

    private final ModelMapper modelMapper;//데이터 변환용
    private final ContentRepository contentRepository;//DB 연결용

    @Override//게시글 등록 구현
    public Long ContentRegister (ContentDTO contentDTO) {//클라이언트로부터 전달된 게시글 정보 포함
        //Board board = modelMapper.map(boardDTO, Board.class);//boardDTO를 Board Entity 변환
        Content content = dtoToEntity (contentDTO);
        Long cno = contentRepository.save (content).getCno ();//게시글을 DB에 저장하고 게시글 Bno를 가져와 Long 타입 저장
        return cno;
    }

    @Override//게시글 조회 구현
    public ContentDTO ContentReadOne (Long cno) {//게시글 번호를 받아 게시글을 조회하고 BoardDTO에 반환
        //board_image까지 조인 처리되는 findByIdWithImages()를 이용
        Optional<Content> result = contentRepository.findByIdWithContentImages (cno);//DB에서 bno로 해당하는 게시글을 찾아 result 반환
        Content content = result.orElseThrow ();//해당 게시글이 없으면 예외 발생
        //BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);//엔티티 객체를 DTO로 변환
        ContentDTO contentDTO = entityToDTO (content);
        return contentDTO;//DTP 반환
    }

    @Override//게시글 수정 구현
    public void contentModify (ContentDTO contentDTO) {//수정할 내용 BoardDTO로 받음
        Optional<Content> result = contentRepository.findById (contentDTO.getCno ());//boardDTO의 bno에 해당하는 게시글을 DB에서 가져옴
        Content content = result.orElseThrow ();//해당 게시글이 없으면 예외 발생
        content.change (contentDTO.getCtitle (), contentDTO.getCcontent ());//엔티티 내 제목, 내용 수정
        //첨부파일의 처리
        content.clearImages ();
        if (contentDTO.getFileNames () != null) {
            for (String fileName : contentDTO.getFileNames ()) {
                String[] arr = fileName.split ("_");
                content.addImage (arr[0], arr[1]);
            }
        }
        contentRepository.save (content);//수정된 내용 DB 저장
    }
    @Override//게시글 삭제 구현
    public void contentRemove (Long cno) {//삭제할 게시물의 bno를 받음
        contentRepository.deleteById (cno);//해당 게시물 DB 삭제
    }

    @Override//게시글 목록/검색 구현
    public ContentPageResponseDTO<ContentDTO> contentList (ContentPageRequestDTO contentPageRequestDTO) {//페이지 네이션과 검색 조건 처리

        String[] types = contentPageRequestDTO.getTypes ();//검색 조건 추출
        String keyword = contentPageRequestDTO.getKeyword ();//키워드 추출
        Pageable cPageable = contentPageRequestDTO.getCPageable ("cno");//페이지 네이션 정보 추출

        //searchAll() 호출하여 게시글 검색, 검색 조건, 키워드 바탕으로 검색하고 페이지네이션 적용한 결과를 result 반환
        Page<Content> result = contentRepository.searchContentAll (types, keyword, cPageable);

        // 검색 결과인 Page<Board>를 BoardDTO로 변환하여 dtoList에 저장합니다.
        List<ContentDTO> dtoCList = result.getContent ().stream ()
                .map (content -> modelMapper.map (content, ContentDTO.class)).collect (Collectors.toList ());
        // PageResponseDTO 객체를 생성하고, 페이지 관련 정보와 변환된 게시글 목록, 전체 게시글 수를 설정합니다.
        // 이때, withAll() 메서드를 사용하여 기본 정보를 설정하고,
        // pageRequestDTO, dtoList, total 값을 설정한 뒤 build()로 최종 객체를 생성합니다.
        return ContentPageResponseDTO.<ContentDTO>withContentAll ()
                .contentpageRequestDTO (contentPageRequestDTO)
                .dtoCList (dtoCList)
                .ctotal ((int) result.getTotalElements ())
                .build ();
    }

    @Override//게시글 목록/검색 구현
    public ContentRPageResponseDTO<ContentDTO> contentRList (ContentRPageRequestDTO contentRPageRequestDTO) {//페이지 네이션과 검색 조건 처리

        String[] types = contentRPageRequestDTO.getTypes ();//검색 조건 추출
        String keyword = contentRPageRequestDTO.getKeyword ();//키워드 추출
        Pageable cRPageable = contentRPageRequestDTO.getCRPageable ("cno");//페이지 네이션 정보 추출

        //searchAll() 호출하여 게시글 검색, 검색 조건, 키워드 바탕으로 검색하고 페이지네이션 적용한 결과를 result 반환
        Page<Content> result = contentRepository.searchContentRAll (types, keyword, cRPageable);

        // 검색 결과인 Page<Board>를 BoardDTO로 변환하여 dtoList에 저장합니다.
        List<ContentDTO> dtoCRList = result.getContent ().stream ()
                .map (content -> modelMapper.map (content, ContentDTO.class)).collect (Collectors.toList ());
        // PageResponseDTO 객체를 생성하고, 페이지 관련 정보와 변환된 게시글 목록, 전체 게시글 수를 설정합니다.
        // 이때, withAll() 메서드를 사용하여 기본 정보를 설정하고,
        // pageRequestDTO, dtoList, total 값을 설정한 뒤 build()로 최종 객체를 생성합니다.
        return ContentRPageResponseDTO.<ContentDTO>withContentRAll ()
                .contentRPageRequestDTO (contentRPageRequestDTO)
                .dtoCRList (dtoCRList)
                .crtotal ((int) result.getTotalElements ())
                .build ();
    }

    @Override//PageRequestDTO를 입력받아 검색 조건, 키워드, 페이지 정보 추출
    public ContentPageResponseDTO<ContentListReplyCountDTO> contentListWithReplyCount (ContentPageRequestDTO contentPageRequestDTO) {
        String[] types = contentPageRequestDTO.getTypes ();//검색 조건
        String keyword = contentPageRequestDTO.getKeyword ();//검색어
        Pageable pageable = contentPageRequestDTO.getCPageable ("cno");//페이지 번호, 크기, 정렬 정보
        // result값을 사용하여 PageResponseDTO 객체 생성
        Page<ContentListReplyCountDTO> result = contentRepository.searchWithContentReplyCount (types, keyword, pageable);
        return ContentPageResponseDTO.<ContentListReplyCountDTO>withContentAll ()//빌더 패턴 사용
                .contentpageRequestDTO (contentPageRequestDTO)//페이지 요청 정보 설정
                .dtoCList (result.getContent ())//BoardListReplyCountDTO 객체의 리스트 설정
                .ctotal ((int) result.getTotalElements ())//전체 결과 수 설정
                .build ();
    }

    @Override
    public ContentPageResponseDTO<ContentListAllDTO> contentListWithAll (ContentPageRequestDTO contentPageRequestDTO) {
        String[] types = contentPageRequestDTO.getTypes ();
        String keyword = contentPageRequestDTO.getKeyword ();
        Pageable pageable = contentPageRequestDTO.getCPageable ("cno");
        Page<ContentListAllDTO> result = contentRepository.searchWithContentAll (types, keyword, pageable);
        return ContentPageResponseDTO.<ContentListAllDTO>withContentAll ().contentpageRequestDTO (contentPageRequestDTO).dtoCList (result.getContent ()).ctotal ((int) result.getTotalElements ()).build ();
    }

    @Override
    public ContentPageResponseDTO<ContentListAllDTO> searchWithContentRecent (ContentPageRequestDTO contentPageRequestDTO) {
        String[] types = contentPageRequestDTO.getTypes ();
        String keyword = contentPageRequestDTO.getKeyword ();
        Pageable pageable = contentPageRequestDTO.getCPageable ("cno");
        Page<ContentListAllDTO> result = contentRepository.searchWithContentRecent (types, keyword, pageable);
        return ContentPageResponseDTO.<ContentListAllDTO>withContentAll ().contentpageRequestDTO (contentPageRequestDTO).dtoCList (result.getContent ()).ctotal ((int) result.getTotalElements ()).build ();
    }
}

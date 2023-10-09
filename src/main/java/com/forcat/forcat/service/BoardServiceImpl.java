package com.forcat.forcat.service;

import com.forcat.forcat.dto.board.BoardListReplyCountDTO;
import com.forcat.forcat.entity.Board;
import com.forcat.forcat.dto.board.BoardListAllDTO;
import com.forcat.forcat.repository.BoardRepository;
import com.forcat.forcat.dto.board.BoardDTO;
import com.forcat.forcat.dto.PageRequestDTO;
import com.forcat.forcat.dto.PageResponseDTO;
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
public class BoardServiceImpl implements BoardService{

    private final ModelMapper modelMapper;//데이터 변환용
    private final BoardRepository boardRepository;//DB 연결용

    @Override//게시글 등록 구현
    public Long register(BoardDTO boardDTO) {//클라이언트로부터 전달된 게시글 정보 포함
        if (isUserLoggedIn()) {
            // 로그인 상태인 경우
            Board board = modelMapper.map(boardDTO, Board.class);
            Long bno = boardRepository.save(board).getBno();
            return bno;
        } else {
            // 로그인하지 않은 경우
            Board board = dtoToEntity(boardDTO);
            Long bno = boardRepository.save(board).getBno();
            return bno;
        }
    }

    @Override//게시글 조회 구현
    public BoardDTO readOne(Long bno) {//게시글 번호를 받아 게시글을 조회하고 BoardDTO에 반환
        //board_image까지 조인 처리되는 findByIdWithImages()를 이용
        Optional<Board> result = boardRepository.findByIdWithImages(bno);//DB에서 bno로 해당하는 게시글을 찾아 result 반환
        Board board = result.orElseThrow();//해당 게시글이 없으면 예외 발생
        //BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);//엔티티 객체를 DTO로 변환
        BoardDTO boardDTO = entityToDTO(board);
        return boardDTO;//DTP 반환
    }

    @Override//게시글 수정 구현
    public void modify(BoardDTO boardDTO) {//수정할 내용 BoardDTO로 받음
        Optional<Board> result = boardRepository.findById(boardDTO.getBno());//boardDTO의 bno에 해당하는 게시글을 DB에서 가져옴
        Board board = result.orElseThrow();//해당 게시글이 없으면 예외 발생
        board.change(boardDTO.getTitle(), boardDTO.getContent());//엔티티 내 제목, 내용 수정
        //첨부파일의 처리
        board.clearImages();
        if(boardDTO.getFileNames() != null){
            for (String fileName : boardDTO.getFileNames()) {
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            }
        }
        boardRepository.save(board);//수정된 내용 DB 저장
    }

    @Override//게시글 삭제 구현
    public void remove(Long bno) {//삭제할 게시물의 bno를 받음
        boardRepository.deleteById(bno);//해당 게시물 DB 삭제
    }

    @Override//게시글 목록/검색 구현
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO) {//페이지 네이션과 검색 조건 처리
        String[] types = pageRequestDTO.getTypes();//검색 조건 추출
        String keyword = pageRequestDTO.getKeyword();//키워드 추출
        Pageable pageable = pageRequestDTO.getPageable("bno");//페이지 네이션 정보 추출
        //searchAll() 호출하여 게시글 검색, 검색 조건, 키워드 바탕으로 검색하고 페이지네이션 적용한 결과를 result 반환
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable);
        // 검색 결과인 Page<Board>를 BoardDTO로 변환하여 dtoList에 저장합니다.
        List<BoardDTO> dtoList = result.getContent().stream()
                .map(board -> modelMapper.map(board,BoardDTO.class)).collect(Collectors.toList());

        // PageResponseDTO 객체를 생성하고, 페이지 관련 정보와 변환된 게시글 목록, 전체 게시글 수를 설정합니다.
        // 이때, withAll() 메서드를 사용하여 기본 정보를 설정하고,
        // pageRequestDTO, dtoList, total 값을 설정한 뒤 build()로 최종 객체를 생성합니다.
        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override//PageRequestDTO를 입력받아 검색 조건, 키워드, 페이지 정보 추출
    public PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();//검색 조건
        String keyword = pageRequestDTO.getKeyword();//검색어
        Pageable pageable = pageRequestDTO.getPageable("bno");//페이지 번호, 크기, 정렬 정보
        // result값을 사용하여 PageResponseDTO 객체 생성
        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);
        return PageResponseDTO.<BoardListReplyCountDTO>withAll()//빌더 패턴 사용
                .pageRequestDTO(pageRequestDTO)//페이지 요청 정보 설정
                .dtoList(result.getContent())//BoardListReplyCountDTO 객체의 리스트 설정
                .total((int)result.getTotalElements())//전체 결과 수 설정
                .build();
    }

    @Override
    public PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO) {
        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");
        Page<BoardListAllDTO> result = boardRepository.searchWithAll(types, keyword, pageable);
        return PageResponseDTO.<BoardListAllDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(result.getContent())
                .total((int)result.getTotalElements())
                .build();
    }

    private boolean isUserLoggedIn() {
        // 현재 사용자의 인증 정보를 가져옴
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // UserDetails 타입으로 캐스팅하여 로그인 여부 확인
        return principal instanceof UserDetails;
    }

}

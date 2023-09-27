package com.forcat.forcat.service;

import com.forcat.forcat.entity.Board;
import com.forcat.forcat.repository.BoardRepository;
import com.forcat.forcat.security.dto.BoardDTO;
import com.forcat.forcat.security.dto.PageRequestDTO;
import com.forcat.forcat.security.dto.PageResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
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
        Board board = modelMapper.map(boardDTO, Board.class);//boardDTO를 Board Entity 변환
        Long bno = boardRepository.save(board).getBno();//게시글을 DB에 저장하고 게시글 Bno를 가져와 Long 타입 저장
        return bno;
    }

    @Override//게시글 조회 구현
    public BoardDTO readOne(Long bno) {//게시글 번호를 받아 게시글을 조회하고 BoardDTO에 반환
        Optional<Board> result = boardRepository.findById(bno);//DB에서 bno로 해당하는 게시글을 찾아 result 반환
        Board board = result.orElseThrow();//해당 게시글이 없으면 예외 발생
        BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class);//엔티티 객체를 DTO로 변환
        return boardDTO;//DTP 반환
    }

    @Override//게시글 수정 구현
    public void modify(BoardDTO boardDTO) {//수정할 내용 BoardDTO로 받음
        Optional<Board> result = boardRepository.findById(boardDTO.getBno());//boardDTO의 bno에 해당하는 게시글을 DB에서 가져옴
        Board board = result.orElseThrow();//해당 게시글이 없으면 예외 발생
        board.change(boardDTO.getTitle(), boardDTO.getContent());//엔티티 내 제목, 내용 수정
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
/*

    private final BoardRepository repository; //자동 주입 final
    private final ReplyRepository replyRepository; //자동 주입 final

    @Override
    public Long register(BoardDTO dto) {

        log.info(dto);

        Board board = dtoToEntity(dto);
        repository.save(board);
        return board.getBno();
    }

    @Transactional
    @Override
    public void removeWithReplies(Long bno) { //삭제 기능 구현, 트랜젝션 추가

        //댓글부터 삭제
        replyRepository.deleteByBno(bno);
        repository.deleteById(bno);
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        log.info(pageRequestDTO);

        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board) en[0],(Member) en[1],(Long) en[2]));

        */
/*Page<Object[]> result = repository.getBoardWithReplyCount(
                pageRequestDTO.getPageable(Sort.by("bno").descending()));*//*


//      목록화면 검색처리
        Page<Object[]> result = repository.searchPage(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("bno").descending())  );

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO get(Long bno) {
        Object result = repository.getBoardByBno(bno);
        Object[] arr = (Object[]) result;
        return entityToDTO((Board)arr[0], (Member)arr[1], (Long)arr[2]);
    }

    @Transactional
    @Override
    public void modify(BoardDTO boardDTO) {
        Board board = repository.getOne(boardDTO.getBno());

        board.changeTitle(boardDTO.getTitle());
        board.changeContent(boardDTO.getContent());

        repository.save(board);
    }

*/

}

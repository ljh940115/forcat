package com.forcat.forcat.repository;

import com.forcat.forcat.dto.BoardListReplyCountDTO;
import com.forcat.forcat.entity.Board;
import com.forcat.forcat.dto.BoardDTO;
import com.forcat.forcat.dto.PageRequestDTO;
import com.forcat.forcat.dto.PageResponseDTO;
import com.forcat.forcat.service.BoardService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest//스프링부트 테스트 명시
@Log4j2//로그 사용 명시
public class BoardRepositoryTests {

    @Autowired//빈 생성
    private BoardRepository boardRepository;
    @Autowired
    private BoardService boardService;

    @Test//게시글 추가 테스트
    public void testInsert() {
        IntStream.rangeClosed(1,100).forEach(i -> {//1부터 100까지 생성
            Board board = Board.builder()
                    .title("title..." +i)
                    .content("content..." + i)
                    .writer("user"+ (i % 10))
                    .build();

            Board result = boardRepository.save(board);
            log.info("BNO: " + result.getBno());
        });
    }

    @Test//게시글 조회 테스트
    public void testSelect() {
        Long bno = 100L;//n번째 게시글

        //boardRepository을 사용하여 n번째 게시글을 찾는다.
        //Optional 객체는 null 에러 방지
        Optional<Board> result = boardRepository.findById(bno);
        //result가 null일 경우 board에 담는다.
        Board board = result.orElseThrow();
        log.info(board);
    }

    @Test//게시글 수정 테스트
    public void testUpdate() {
        Long bno = 100L;//n번째 게시글
        //boardRepository을 사용하여 n번째 게시글을 찾는다.
        //Optional 객체는 null 에러 방지
        Optional<Board> result = boardRepository.findById(bno);
        //result가 null일 경우 board에 담는다.
        Board board = result.orElseThrow();
        //board 클래스의 change 메서드 실행
        board.change("update..title 100", "update content 100");
        //업데이트 내용 DB 저장
        boardRepository.save(board);
    }

    @Test//게시글 삭제 테스트
    public void testDelete() {
        Long bno = 2L;
        boardRepository.deleteById(bno);
    }

    @Test//게시글 페이징 테스트
    public void testPaging() {

        //1 page order by bno desc
        //Pageable 0번째 페이지, 10개 게시글, bno기준 내림차순 정렬
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
        //DB에 Pageable 조건 조회
        Page<Board> result = boardRepository.findAll(pageable);

        log.info("total count: "+result.getTotalElements());
        log.info( "total pages:" +result.getTotalPages());
        log.info("page number: "+result.getNumber());
        log.info("page size: "+result.getSize());

        List<Board> todoList = result.getContent();

        todoList.forEach(board -> log.info(board));
    }

    @Test
    public void testSearch1() {
        //2 page order by bno desc
        Pageable pageable = PageRequest.of(1,10, Sort.by("bno").descending());
        boardRepository.search1(pageable);
    }

    @Test//Querydsl 검색 테스트
    public void testSearchAll() {
        String[] types = {"t","c","w"};//검색할 항목 명시
        String keyword = "1";//검색 키워드 명시
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());//페이지 네이션 정보 명시
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable );
    }

    @Test
    public void testSearchAll2() {
        String[] types = {"t","c","w"};//검색할 항목 명시
        String keyword = "1";//검색 키워드 명시
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());//페이지 네이션 정보 명시
        Page<Board> result = boardRepository.searchAll(types, keyword, pageable );
        //total pages
        log.info(result.getTotalPages());
        //pag size
        log.info(result.getSize());
        //pageNumber
        log.info(result.getNumber());
        //prev next
        log.info(result.hasPrevious() +": " + result.hasNext());
        result.getContent().forEach(board -> log.info(board));
    }

    @Test//게시글 등록 테스트
    public void testRegister(){
        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()//BoardDTO 객체 생성 및 초기화, 정보를 담는다.
                .title("Sample Title...")
                .content("Sample Content...")
                .writer("user00")
                .build();

        Long bno = boardService.register(boardDTO);//boardService의 register() 메서드를 호출하여 boardDTO를 이용해 게시글을 등록
        log.info("bno : " + bno);
    }

    @Test//게시글 조회 테스트
    public void testReadOne(){
        Long bno = 2L;
        boardService.readOne(bno);
        log.info(boardService.readOne(bno));
    }

    @Test//게시글 수정 테스트
    public void testModify(){
        //변경에 필요한 데이터만 입력
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(100L)
                .title("Updated Title...100")
                .content("Updated Content...100")
                .build();

                boardService.modify(boardDTO);
    }

    //삭제 테스트 cascade 적용 후 해볼 것

    @Test//게시글 목록/검색 테스트
    public void  testList(){
        // PageRequestDTO 객체를 생성하여 테스트에 필요한 페이지네이션 및 검색 조건을 설정
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")//검색 조건
                .keyword("1")//검색 키워드
                .page(1)//현재 페이지 번호
                .size(10)//페이지 크기
                .build();

        // 게시글 목록 조회 또는 검색을 수행하고, 결과를 responseDTO에 저장
        PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
        log.info(responseDTO);
    }

    @Test
    public void testSearchReplyCount() {

        String[] types = {"t","c","w"};

        String keyword = "1";

        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable );

        //total pages
        log.info(result.getTotalPages());
        //pag size
        log.info(result.getSize());
        //pageNumber
        log.info(result.getNumber());
        //prev next
        log.info(result.hasPrevious() +": " + result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }
/*
    @Test
    public void testInsertWithImages() {

        Board board = Board.builder()
                .title("Image Test")
                .content("첨부파일 테스트")
                .writer("tester")
                .build();

        for (int i = 0; i < 3; i++) {

            board.addImage(UUID.randomUUID().toString(), "file"+i+".jpg");

        }//end for

        boardRepository.save(board);
    }

//    @Test
//    public void testReadWithImages() {
//
//        //반드시 존재하는 bno로 확인
//        Optional<Board> result = boardRepository.findById(1L);
//
//        Board board = result.orElseThrow();
//
//        log.info(board);
//        log.info("--------------------");
//        log.info(board.getImageSet());
//    }
    @Test
    public void testReadWithImages() {

        //반드시 존재하는 bno로 확인
        Optional<Board> result = boardRepository.findByIdWithImages(1L);

        Board board = result.orElseThrow();

        log.info(board);
        log.info("--------------------");
        for (BoardImage boardImage : board.getImageSet()) {
            log.info(boardImage);
        }
    }

    @Transactional
    @Commit
    @Test
    public void testModifyImages() {

        Optional<Board> result = boardRepository.findByIdWithImages(1L);

        Board board = result.orElseThrow();

        //기존의 첨부파일들은 삭제
        board.clearImages();

        //새로운 첨부파일들
        for (int i = 0; i < 2; i++) {

            board.addImage(UUID.randomUUID().toString(), "updatefile"+i+".jpg");
        }

        boardRepository.save(board);

    }

    @Test
    @Transactional
    @Commit
    public void testRemoveAll() {

        Long bno = 1L;

        replyRepository.deleteByBoard_Bno(bno);

        boardRepository.deleteById(bno);

    }

    @Test
    public void testInsertAll() {

        for (int i = 1; i <= 100; i++) {

            Board board  = Board.builder()
                    .title("Title.."+i)
                    .content("Content.." + i)
                    .writer("writer.." + i)
                    .build();

            for (int j = 0; j < 3; j++) {

                if(i % 5 == 0){
                    continue;
                }
                board.addImage(UUID.randomUUID().toString(),i+"file"+j+".jpg");
            }
            boardRepository.save(board);

        }//end for
    }

    @Transactional
    @Test
    public void testSearchImageReplyCount() {
        Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());
        //boardRepository.searchWithAll(null, null,pageable);
        Page<BoardListAllDTO> result = boardRepository.searchWithAll(null,null,pageable);
        log.info("---------------------------");
        log.info(result.getTotalElements());
        result.getContent().forEach(boardListAllDTO -> log.info(boardListAllDTO));
    }*/
}











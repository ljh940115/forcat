package com.forcat.forcat.service;

import com.forcat.forcat.dto.board.BoardDTO;
import com.forcat.forcat.dto.PageRequestDTO;
import com.forcat.forcat.dto.PageResponseDTO;
import com.forcat.forcat.dto.board.BoardImageDTO;
import com.forcat.forcat.dto.board.BoardListAllDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegisterWithImages () {
        log.info (boardService.getClass ().getName ());
        BoardDTO boardDTO = BoardDTO.builder ().title ("File...Sample Title...").content ("Sample Content...").writer ("user00").build ();
        boardDTO.setFileNames (Arrays.asList (UUID.randomUUID () + "_aaa.jpg", UUID.randomUUID () + "_bbb.jpg", UUID.randomUUID () + "_bbb.jpg"));
        Long bno = boardService.register (boardDTO);
        log.info ("bno: " + bno);
    }

    @Test//게시물 조회
    public void testReadAll () {
        Long bno = 101L;
        BoardDTO boardDTO = boardService.readOne (bno);
        log.info (boardDTO);
        for (String fileName : boardDTO.getFileNames ()) {
            log.info (fileName);
        }//end for
    }

    @Test//게시물 수정
    public void testModify () {
        //변경에 필요한 데이터
        BoardDTO boardDTO = BoardDTO.builder ().bno (101L).title ("Updated....101").content ("Updated content 101...").build ();
        //첨부파일을 하나 추가
        boardDTO.setFileNames (List.of (UUID.randomUUID () + "_zzz.jpg"));
        boardService.modify (boardDTO);
    }


    @Test//게시물 삭제, 댓글이 존재하지 않는 경우
    public void testRemoveAll () {
        Long bno = 1L;
        boardService.remove (bno);
    }

    @Test//게시물 목록 처리
    public void testListWithAll () {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder ()//페이징 관련 정보 담음
                .page (1)//1페이지
                .size (10)//페이지 크기
                .build ();
        //listWithAll 메서드를 호출하여 페이지 관련 정보를 전달하고, 이를 통해 게시물 목록을 검색
        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll (pageRequestDTO);
        //게시물 정보와 이미지 정보가 들어있는 DTO 리스트 추출
        List<BoardListAllDTO> dtoList = responseDTO.getDtoList ();
        //BoardListAllDTO 객체를 순회하며 처리
        dtoList.forEach (boardListAllDTO -> {
            log.info (boardListAllDTO.getBno () + ":" + boardListAllDTO.getTitle ());
            if (boardListAllDTO.getBoardImages () != null) {
                for (BoardImageDTO boardImage : boardListAllDTO.getBoardImages ()) {
                    log.info (boardImage);
                }
            }
            log.info ("-------------------------------");
        });
    }
}

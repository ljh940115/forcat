package com.forcat.forcat.service;

import com.forcat.forcat.dto.BoardDTO;
import com.forcat.forcat.dto.BoardListReplyCountDTO;
import com.forcat.forcat.dto.PageRequestDTO;
import com.forcat.forcat.dto.PageResponseDTO;
import com.forcat.forcat.entity.Board;
import com.forcat.forcat.dto.BoardListAllDTO;

import java.util.List;
import java.util.stream.Collectors;

public interface BoardService {

    //게시글 등록
    Long register(BoardDTO dto);

    //게시글 조회
    BoardDTO readOne(Long bno);

    //게시글 수정
    void modify(BoardDTO boardDTO);

    //게시글 삭제
    void remove(Long bno);

    //페이지 목록/검색
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

/*

    void removeWithReplies(Long bno); //삭제 기능
*/

    //댓글의 숫자까지 처리
    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);

    //게시글의 이미지와 댓글의 숫자까지 처리
    PageResponseDTO<BoardListAllDTO> listWithAll(PageRequestDTO pageRequestDTO);

    default Board dtoToEntity(BoardDTO boardDTO){//DTO 객체를 Entity로 변환

        Board board = Board.builder()
                .bno(boardDTO.getBno())
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .writer(boardDTO.getWriter())
                .build();

        if(boardDTO.getFileNames() != null){//파일 이름 목록이 비어있는지 확인
            boardDTO.getFileNames().forEach(fileName -> {//비어있지 않으면 파일 이름 분리, 엔티티에 이미지 정보 추가
                String[] arr = fileName.split("_");
                board.addImage(arr[0], arr[1]);
            });
        }
        return board;
    }

 default  BoardDTO entityToDTO(Board board) {//Entity 객체를 DTO로 변환

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getWriter())
                .reg_time(board.getRegTime())
                .update_time(board.getUpdateTime())
                .build();

     //getImageSet를 통해 게시물에 연결된 이미지 목록을 가져와 파일 이름 목록으로 변환
     List<String> fileNames =
             board.getImageSet().stream().sorted().map(boardImage ->
                     boardImage.getUuid()+"_"+boardImage.getFileName()).collect(Collectors.toList());

     boardDTO.setFileNames(fileNames);//fileNames 목록을 boardDTO에 설정

        return boardDTO;
    }
/*
    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO); //목록 처리

    default  Board dtoToEntity(BoardDTO dto){
        Member member = Member.builder()
                .email(dto.getWriterEmail())
                .build();

        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();

        return board;
    }

    default BoardDTO entityToDTO(Board board, Member member, Long replyCount) {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .reg_time(board.getReg_time())
                .update_time(board.getUpdate_time())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue()) //int로 처리하도록
                .build();

        return boardDTO;
    }

    BoardDTO get(Long bno);

    //수정 메서드
    void modify(BoardDTO boardDTO);*/
}

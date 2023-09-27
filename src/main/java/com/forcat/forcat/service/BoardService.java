package com.forcat.forcat.service;

import com.forcat.forcat.security.dto.BoardDTO;
import com.forcat.forcat.security.dto.PageRequestDTO;
import com.forcat.forcat.security.dto.PageResponseDTO;

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

/*    default Board dtoToEntity(BoardDTO dto){
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
    }*/

   /* default  BoardDTO entityToDTO(Board board, Member member, Long replyCount) {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue())
                .build();

        return boardDTO;
    }*//*

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
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
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

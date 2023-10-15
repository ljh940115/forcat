package com.forcat.forcat.repository.Search;

import com.forcat.forcat.dto.board.BoardListReplyCountDTO;
import com.forcat.forcat.entity.Board;
import com.forcat.forcat.dto.board.BoardListAllDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

//기존 Repository - Querydel 연동
//단순 페이지 처리 기능
public interface SearchBoardRepository {

    Page<Board> search1 (Pageable pageable);

    Page<Board> searchAll (String[] types, String keyword, Pageable pageable);//Querydsl로 검색 처리

    Page<BoardListReplyCountDTO> searchWithReplyCount (String[] types, String keyword, Pageable pageable);

    Page<BoardListAllDTO> searchWithAll (String[] types, String keyword, Pageable pageable);
}

package com.forcat.forcat.repository.Search;

import com.forcat.forcat.entity.Board;
import com.forcat.forcat.entity.QBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Page<Board> search1(Pageable pageable) {
        QBoard board = QBoard.board;//Q도메인 객체
        JPQLQuery<Board> query = from(board);//select...from board
        query.where(board.title.contains("1"));//where title like...

        //Querydsl로 Pageable 처리, paging
        this.getQuerydsl().applyPagination(pageable, query);

        List<Board> list = query.fetch();//fetch() JPQL 실행
        long count = query.fetchCount();//fetchCount() count 쿼리 실행
        return null;
    }

    @Override
    public Page<Board> searchAll(String[] types, String keyword, Pageable pageable) {
        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);//QBoard를 이용하여 JPQLQuery 생성

        if((types != null && types.length >0) && keyword != null){//타입과 키워드 존재 확인
            BooleanBuilder booleanBuilder = new BooleanBuilder();//여러 검색 조건 결합용 객체 생성
            for(String type:types){//types 배열을 순회하며 각 타입에 따라 검색 조건 추가
                switch (type){//type 값에 따라 검색 조건 적용
                    case "t"://게시글 제목 검색 조건 추가
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c"://게시글 내용 검색 조건 추가
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w"://게시글 작성자 검색 조건 추가
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }
            }//end fot
            query.where(booleanBuilder);//검색 조건을 쿼리에 추가, OR로 결합됨
        }//end if
        //bno >0
        query.where(board.bno.gt(0L));//게시글 번호가 0보다 큰 경우 추가 검색
        //paging
        this.getQuerydsl().applyPagination(pageable, query);//페이지 네이션 적용하기 위해 pageable 사용, 쿼리에 페이지네이션 정보 추가
        List<Board> list = query.fetch();//쿼리 실행하여 검색 결과 가져옴
        long count = query.fetchCount();//검색된 결과 총 개수 가져옴
        return new PageImpl<>(list, pageable, count);//게시글 목록, 페이지네이션 정보, 검색 결과 전체 개수를 담아 보낸다.
    }
}


package com.forcat.forcat.repository;

import com.forcat.forcat.dto.content.ContentImageDTO;
import com.forcat.forcat.dto.content.ContentListAllDTO;
import com.forcat.forcat.dto.content.ContentListReplyCountDTO;
import com.forcat.forcat.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchContentRepositoryImpl extends QuerydslRepositorySupport implements SearchContentRepository {

    public SearchContentRepositoryImpl () {
        super (Content.class);
    }

    @Override
    public Page<Content> searchContent1 (Pageable pageable) {
        QContent content = QContent.content;//Q도메인 객체
        JPQLQuery<Content> query = from (content);//select...from board
        query.where (content.ctitle.contains ("1"));//where title like...
        //Querydsl로 Pageable 처리, paging
        this.getQuerydsl ().applyPagination (pageable, query);
        List<Content> list = query.fetch ();//fetch() JPQL 실행
        long count = query.fetchCount ();//fetchCount() count 쿼리 실행
        return null;
    }

    @Override
    public Page<Content> searchContentAll (String[] types, String keyword, Pageable pageable) {
        QContent content = QContent.content;
        JPQLQuery<Content> query = from (content);//QBoard를 이용하여 JPQLQuery 생성
        if ((types != null && types.length > 0) && keyword != null) {//타입과 키워드 존재 확인
            BooleanBuilder booleanBuilder = new BooleanBuilder ();//여러 검색 조건 결합용 객체 생성
            for (String type : types) {//types 배열을 순회하며 각 타입에 따라 검색 조건 추가
                switch (type) {//type 값에 따라 검색 조건 적용
                    case "t"://게시글 제목 검색 조건 추가
                        booleanBuilder.or (content.ctitle.contains (keyword));
                        break;
                    case "c"://게시글 내용 검색 조건 추가
                        booleanBuilder.or (content.ccontent.contains (keyword));
                        break;
                    case "w"://게시글 작성자 검색 조건 추가
                        booleanBuilder.or (content.cwriter.contains (keyword));
                        break;
                }
            }//end fot
            query.where (booleanBuilder);//검색 조건을 쿼리에 추가, OR로 결합됨
        }//end if
        //bno >0
        query.where (content.cno.gt (0L));//게시글 번호가 0보다 큰 경우 추가 검색
        //paging
        this.getQuerydsl ().applyPagination (pageable, query);//페이지 네이션 적용하기 위해 pageable 사용, 쿼리에 페이지네이션 정보 추가
        List<Content> list = query.fetch ();//쿼리 실행하여 검색 결과 가져옴
        long count = query.fetchCount ();//검색된 결과 총 개수 가져옴
        return new PageImpl<> (list, pageable, count);//게시글 목록, 페이지네이션 정보, 검색 결과 전체 개수를 담아 보낸다.
    }

    @Override
    public Page<ContentListReplyCountDTO> searchWithContentReplyCount (String[] types, String keyword, Pageable pageable) {
        QContent content = QContent.content;//QBoard 객체 생성
        QContentReply contentReply = QContentReply.contentReply;//QReply 객체 생성
        JPQLQuery<Content> query = from (content);//from(board)을 사용하여 게시판(Board) 테이블을 대상으로 하는 JPQLQuery를 생성
        query.leftJoin (contentReply).on (contentReply.content.eq (content));//게시글, 댓글 테이블 조인
        query.groupBy (content);//결과를 게시글로 그룹화
        if ((types != null && types.length > 0) && keyword != null) {//검색 조건 설정
            BooleanBuilder booleanBuilder = new BooleanBuilder ();//or 연산자를 사용하여 검색 조건 추가
            for (String type : types) {//types 배열에 따라 어떤 항목으로 검색할지 결정
                switch (type) {
                    case "t":
                        booleanBuilder.or (content.ctitle.contains (keyword));
                        break;
                    case "c":
                        booleanBuilder.or (content.ccontent.contains (keyword));
                        break;
                    case "w":
                        booleanBuilder.or (content.cwriter.contains (keyword));
                        break;
                }
            }//end for
            query.where (booleanBuilder);
        }
        //bno > 0
        query.where (content.cno.gt (0L));//게시물 번호가 0보다 큰 경우 조건 추가
        //Brojections.bean 메서드 사용하여 BoardListReplyCountDTO 클래스 매핑할 프로젝션 설정
        JPQLQuery<ContentListReplyCountDTO> dtoQuery = query.select (Projections.bean (ContentListReplyCountDTO.class, content.cno, content.ctitle, content.cwriter, content.regDate, contentReply.count ().as ("contentReplyCount")));
        this.getQuerydsl ().applyPagination (pageable, dtoQuery);//페이지네이션 적용
        List<ContentListReplyCountDTO> dtoCList = dtoQuery.fetch ();//쿼리 결과 리스트 형태로 가져옴
        long count = dtoQuery.fetchCount ();//전체 결과 수 가져옴
        return new PageImpl<> (dtoCList, pageable, count);//결과 리스트, 페이지 정보, 전체 결과 수를 포함하는 페이지 객체 생성하고 반환
    }

    //다대일 문제 확인
    @Override//BoardListAllDTO 타입의 페이지(Page)를 반환하는 메서드, 게시물 목록과 댓글 수 정보가 포함
    public Page<ContentListAllDTO> searchWithContentAll (String[] types, String keyword, Pageable pageable) {
        QContent content = QContent.content;//게시물 엔티티
        QContentReply contentReply = QContentReply.contentReply;//댓글 엔티티
        JPQLQuery<Content> contentJPQLQuery = from (content);//게시물을 조회하기 위한 쿼리를 생성
        contentJPQLQuery.leftJoin (contentReply).on (contentReply.content.eq (content)); //게시물과 댓글에 왼쪽 조인
        if ((types != null && types.length > 0) && keyword != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder (); // (
            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or (content.ctitle.contains (keyword));
                        break;
                    case "c":
                        booleanBuilder.or (content.ccontent.contains (keyword));
                        break;
                    case "w":
                        booleanBuilder.or (content.cwriter.contains (keyword));
                        break;
                }
            }//end for
            contentJPQLQuery.where (booleanBuilder);
        }

        contentJPQLQuery.groupBy (content);// 게시물을 그룹화
        getQuerydsl ().applyPagination (pageable, contentJPQLQuery); //페이지 정보에 대한 쿼리를 생성
        //게시물과 댓글 수를 함께 조회, select는 게시물과 댓글 수를 함께 선택하도록 설정
        JPQLQuery<Tuple> tupleJPQLQuery = contentJPQLQuery.select (content, contentReply.countDistinct ());
        List<Tuple> tupleList = tupleJPQLQuery.fetch ();// 쿼리를 실행하고 결과를 가져와 tupleList라는 리스트에 저장
        //검색된 결과를 BoardListAllDTO 객체로 변환하고, 리스트에 담음 각 게시물의 정보와 댓글 수를 BoardListAllDTO에 매핑
        List<ContentListAllDTO> dtoCList = tupleList.stream ().map (tuple -> {
            Content content1 = tuple.get (content);
            long replyCount = tuple.get (1, Long.class);
            ContentListAllDTO dto = ContentListAllDTO.builder ().cno (content1.getCno ()).ctitle (content1.getCtitle ()).cwriter (content1.getCwriter ()).regDate (content1.getRegDate ()).creplyCount (replyCount).build ();
            //BoardImage를 BoardImageDTO 처리할 부분
            List<ContentImageDTO> imageDTOS = content1.getImageSet ().stream ().sorted ().map (contentImage -> ContentImageDTO.builder ().uuid (contentImage.getUuid ()).fileName (contentImage.getFileName ()).ord (contentImage.getOrd ()).build ()).collect (Collectors.toList ());
            dto.setContentImages (imageDTOS);
            return dto;
        }).collect (Collectors.toList ());
        //전체 결과의 개수를 가져오는 쿼리를 실행하고 totalCount에 저장
        long totalCount = contentJPQLQuery.fetchCount ();
        //최종적으로 변환된 BoardListAllDTO 리스트와 페이징 정보, 전체 결과의 개수를 사용하여 PageImpl 객체를 생성하여 반환
        return new PageImpl<> (dtoCList, pageable, totalCount);
    }

    @Override//BoardListAllDTO 타입의 페이지(Page)를 반환하는 메서드, 게시물 목록과 댓글 수 정보가 포함
    public Page<ContentListAllDTO> searchWithContentRecent (String[] types, String keyword, Pageable pageable) {
        QContent content = QContent.content;//게시물 엔티티
        QContentReply contentReply = QContentReply.contentReply;//댓글 엔티티
        JPQLQuery<Content> contentJPQLQuery = from (content);//게시물을 조회하기 위한 쿼리를 생성
        contentJPQLQuery.leftJoin (contentReply).on (contentReply.content.eq (content)); //게시물과 댓글에 왼쪽 조인
        if ((types != null && types.length > 0) && keyword != null) {
            BooleanBuilder booleanBuilder = new BooleanBuilder (); // (
            for (String type : types) {
                switch (type) {
                    case "t":
                        booleanBuilder.or (content.ctitle.contains (keyword));
                        break;
                    case "c":
                        booleanBuilder.or (content.ccontent.contains (keyword));
                        break;
                    case "w":
                        booleanBuilder.or (content.cwriter.contains (keyword));
                        break;
                }
            }//end for
            contentJPQLQuery.where (booleanBuilder);
        }
        contentJPQLQuery.groupBy (content);// 게시물을 그룹화
        getQuerydsl ().applyPagination (pageable, contentJPQLQuery); //페이지 정보에 대한 쿼리를 생성
        //게시물과 댓글 수를 함께 조회, select는 게시물과 댓글 수를 함께 선택하도록 설정
        JPQLQuery<Tuple> tupleJPQLQuery = contentJPQLQuery.select (content, contentReply.countDistinct ());
        List<Tuple> tupleList = tupleJPQLQuery.fetch ();// 쿼리를 실행하고 결과를 가져와 tupleList라는 리스트에 저장
        //검색된 결과를 BoardListAllDTO 객체로 변환하고, 리스트에 담음 각 게시물의 정보와 댓글 수를 BoardListAllDTO에 매핑
        List<ContentListAllDTO> dtoCList = tupleList.stream ().map (tuple -> {
            Content content1 = tuple.get (content);
            long replyCount = tuple.get (1, Long.class);
            ContentListAllDTO dto = ContentListAllDTO.builder ().cno (content1.getCno ()).ctitle (content1.getCtitle ()).cwriter (content1.getCwriter ()).regDate (content1.getRegDate ()).creplyCount (replyCount).build ();
            //BoardImage를 BoardImageDTO 처리할 부분
            List<ContentImageDTO> imageDTOS = content1.getImageSet ().stream ().sorted ().map (contentImage -> ContentImageDTO.builder ().uuid (contentImage.getUuid ()).fileName (contentImage.getFileName ()).ord (contentImage.getOrd ()).build ()).collect (Collectors.toList ());
            dto.setContentImages (imageDTOS);
            return dto;
        }).collect (Collectors.toList ());
        //전체 결과의 개수를 가져오는 쿼리를 실행하고 totalCount에 저장
        long totalCount = contentJPQLQuery.fetchCount ();
        //최종적으로 변환된 BoardListAllDTO 리스트와 페이징 정보, 전체 결과의 개수를 사용하여 PageImpl 객체를 생성하여 반환
        return new PageImpl<> (dtoCList, pageable, totalCount);
    }
}
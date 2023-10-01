package com.forcat.forcat.repository;

import com.forcat.forcat.dto.BoardListReplyCountDTO;
import com.forcat.forcat.entity.Board;
import com.forcat.forcat.entity.Reply;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;

@SpringBootTest//테스트 클래스
@Log4j2//로그 사용
public class ReplyRepositoryTests {

    @Autowired//객체 생성
    private ReplyRepository replyRepository;

    @Test
    public void testInsert() {

        //실제 DB에 있는 bno
        Long bno  = 200L;

        Board board = Board.builder().bno(bno).build();

        Reply reply = Reply.builder()
                .board(board)
                .replyText("댓글.....")
                .replyer("replyer1")
                .build();

        replyRepository.save(reply);
    }

    @Transactional//동시 처리
    @Test
    public void testBoardReplies() {

        Long bno = 2L;
        //페이징 처리용 객체 생성, 0번째 페이지, 페이지 당 10개 댓글, rno 기준 내림차순
        Pageable pageable = PageRequest.of(0,10, Sort.by("rno").descending());
        //게시글 번호, 페이징 정보 기반 댓글 가져온다.
        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);
        //조회한 댓글 목록에서 댓글을 하나씩 출력
        result.getContent().forEach(reply -> {
            log.info(reply);
        });
    }
}

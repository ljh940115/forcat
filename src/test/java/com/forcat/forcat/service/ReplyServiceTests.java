package com.forcat.forcat.service;

import com.forcat.forcat.dto.board.ReplyDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest//테스트 실행
@Log4j2//로그 출력
public class ReplyServiceTests {

    @Autowired
    private ReplyService replyService;//객체 생성

    @Test
    public void testRegister () {
        ReplyDTO replyDTO = ReplyDTO.builder ().replyText ("ReplyDTO Text").replyer ("replyer").bno (200L).build ();
        log.info (replyService.register (replyDTO));
    }
}

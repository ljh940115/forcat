package com.forcat.forcat.controller;

import com.forcat.forcat.dto.PageRequestDTO;
import com.forcat.forcat.dto.PageResponseDTO;
import com.forcat.forcat.dto.board.ReplyDTO;
import com.forcat.forcat.service.ReplyService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController//REST 컨트롤러
@RequestMapping ("/replies")
@Log4j2//로그 사용
@RequiredArgsConstructor//final, notnull 필드 생성자 자동 생성
public class ReplyController {

    private final ReplyService replyService;//ReplyService 객체 생성

    @ApiOperation (value = "Replies POST", notes = "POST 방식으로 댓글 등록")//스웩 문서화 명시
    @PostMapping (value = "/", consumes = MediaType.APPLICATION_JSON_VALUE)//HTTP POST 요청 처리
    public Map<String, Long> register (@Valid @RequestBody ReplyDTO replyDTO, BindingResult bindingResult)//ReplyDTO 수집할 때 유효성 검사 적용
            throws
            BindException {//JSON 문자열을 ReplyDTO 변환, 문제가 발생하면 BindException로 넘김
        log.info (replyDTO);
        if (bindingResult.hasErrors ()) {
            throw new BindException (bindingResult);
        }
        Map<String, Long> resultMap = new HashMap<> ();
        Long rno = replyService.register (replyDTO);//유효성 검사를 통과한 경우 댓글을 등록하고 rno를 반환
        resultMap.put ("rno", 100L);
        return resultMap;//메소드 리턴값에 문제가 있다면 @RESTControllerAdvice가 처리하고 정상 결과만 리턴
    }

    //특정 게시물 댓글 목록 조회
    @ApiOperation (value = "Replies of Board", notes = "GET 방식으로 특정 게시물의 댓글 목록")//스웩 문서화
    @GetMapping (value = "/list/{bno}")
    public PageResponseDTO<ReplyDTO> getList (@PathVariable ("bno") Long bno, PageRequestDTO pageRequestDTO) {
        //특정 게시물(bno)에 대한 댓글 목록을 조회, PageResponseDTO<ReplyDTO> 형태로 반환
        PageResponseDTO<ReplyDTO> responseDTO = replyService.getListOfBoard (bno, pageRequestDTO);
        return responseDTO;
    }

    //특정 댓글 조회
    @ApiOperation (value = "Read Reply", notes = "GET 방식으로 특정 댓글 조회")
    @GetMapping ("/{rno}")
    public ReplyDTO getReplyDTO (@PathVariable ("rno") Long rno) {//@PathVariable("rno")를 사용하여 경로 변수인 rno를 메서드 파라미터로 받는다.
        ReplyDTO replyDTO = replyService.read (rno);//특정 댓글(rno)을 조회, ReplyDTO로 반환
        return replyDTO;
    }

    //특정 댓글 삭제
    @ApiOperation (value = "Delete Reply", notes = "DELETE 방식으로 특정 댓글 삭제")//스웩 문서 설정
    @DeleteMapping ("/{rno}")//HTTP DELETE 메서드 처리, 경로 변수 사용
    public Map<String, Long> remove (@PathVariable ("rno") Long rno) {//PathVariable는 경로 변수와 연결, Map<String,Long>=rno:123 형식
        replyService.remove (rno);//특정 댓글(rno)을 삭제,
        Map<String, Long> resultMap = new HashMap<> ();//삭제한 댓글의 rno를 담을 Map 객체를 생성
        resultMap.put ("rno", rno);//resultMap에 삭제한 댓글의 rno 값을 저장
        return resultMap;//rno 값을 담고 있는 resultMap을 반환
    }

    //특정 댓글 수정
    @ApiOperation (value = "Modify Reply", notes = "PUT 방식으로 특정 댓글 수정")
    @PutMapping (value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Long> remove (@PathVariable ("rno") Long rno, @RequestBody ReplyDTO replyDTO) {
        replyDTO.setRno (rno);//번호를 일치시킴
        replyService.modify (replyDTO);//댓글을 수정
        Map<String, Long> resultMap = new HashMap<> ();
        resultMap.put ("rno", rno);
        return resultMap;
    }
}

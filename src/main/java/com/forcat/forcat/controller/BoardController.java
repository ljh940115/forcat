package com.forcat.forcat.controller;

import com.forcat.forcat.dto.BoardDTO;
import com.forcat.forcat.dto.BoardListReplyCountDTO;
import com.forcat.forcat.dto.PageRequestDTO;
import com.forcat.forcat.dto.PageResponseDTO;
import com.forcat.forcat.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller//컨트롤러 명시
@RequestMapping("/board")
@Log4j2//로그 사용 명시
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /*게시글 목록, pageRequestDTO를 이용해 페이징 처리 및 검색*/
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        //PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO); 페이지 목록만 표시
        PageResponseDTO<BoardListReplyCountDTO> responseDTO = boardService.listWithReplyCount(pageRequestDTO);//페이지 목록 및 댓글 표시
        log.info(responseDTO);
        model.addAttribute("responseDTO", responseDTO);
    }

    /*게시글 등록*/
    @GetMapping("/register")
    public void registerGET(){

    }

    @PostMapping("/register")
    public String registerPost(@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes){

        log.info("board POST register.......");

        if(bindingResult.hasErrors()) {
            log.info("has errors.......");
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );
            return "redirect:/board/register";
        }

        log.info(boardDTO);

        Long bno  = boardService.register(boardDTO);

        redirectAttributes.addFlashAttribute("result", bno);

        return "redirect:/board/list";
    }

    /*게시글 조회, 수정*/
    @GetMapping({"/read", "modify"})
    public void read(Long bno, PageRequestDTO pageRequestDTO, Model model){
        //게시글 조회하기 위한 서비스 객체 생성
        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);
        //모델 객체에 dto라는 이름으로 boardDTO를 전달
        model.addAttribute("dto", boardDTO);
    }

    /*게시글 수정*/
    @PostMapping("/modify")
    public String modify( @Valid BoardDTO boardDTO,//수정할 게시글 정보
                          BindingResult bindingResult,//유효성 검사
                          PageRequestDTO pageRequestDTO,//페이징
                          RedirectAttributes redirectAttributes){//리다이텍트 시 데이터 전달
        log.info("board modify post......." + boardDTO);

        if(bindingResult.hasErrors()) {//유효성 오류 발생 시 처리
            log.info("has errors.......");
            String link = pageRequestDTO.getLink();//페이징 정보 link 저장
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors() );//리다이텍트 시 오류 정보 전달
            redirectAttributes.addAttribute("bno", boardDTO.getBno());//리다이렉트 시 수정할 게시글 번호 전달
            return "redirect:/board/modify?"+link;//리다이텍트하면서 오류 정보와 게시글 번호 전달
        }
        
        boardService.modify(boardDTO);//오류가 없는 경우 boardDTO 정보 사용하여 게시글 수정 서비스 호출
        redirectAttributes.addFlashAttribute("result", "modified");//게시글 수정하면 result 속성 추가
        redirectAttributes.addAttribute("bno", boardDTO.getBno());//수정된 게시글 번호 전달
        return "redirect:/board/read";//게시글 수정이 완료되면 게시글 조회 페이지 리다이렉트
    }

    /*게시글 삭제*/
    @PostMapping("/remove")
    public String remove(Long bno, RedirectAttributes redirectAttributes) {

        log.info("remove post.. " + bno);

        boardService.remove(bno);

        redirectAttributes.addFlashAttribute("removeresult", "removed");//게시글 삭제 성공하면 result 반환

    return "redirect:/board/list";

    }
}

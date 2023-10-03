package com.forcat.forcat.controller;

import com.forcat.forcat.dto.BoardDTO;
import com.forcat.forcat.dto.BoardListReplyCountDTO;
import com.forcat.forcat.dto.PageRequestDTO;
import com.forcat.forcat.dto.PageResponseDTO;
import com.forcat.forcat.entity.BoardListAllDTO;
import com.forcat.forcat.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

@Controller//컨트롤러 명시
@RequestMapping("/board")
@Log4j2//로그 사용 명시
@RequiredArgsConstructor
public class BoardController {

    @Value("${com.forcat.upload.path}")// import 시에 springframework으로 시작하는 Value
    private String uploadPath;

    private final BoardService boardService;

    /*게시글 목록, pageRequestDTO를 이용해 페이징 처리 및 검색*/
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        //PageResponseDTO<BoardDTO> responseDTO = boardService.list(pageRequestDTO);
        PageResponseDTO<BoardListAllDTO> responseDTO =
                boardService.listWithAll(pageRequestDTO);
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
    public String remove(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {
        Long bno  = boardDTO.getBno();
        log.info("remove post.. " + bno);
        boardService.remove(bno);
        //게시물이 삭제되었다면 첨부 파일 삭제
        log.info(boardDTO.getFileNames());
        List<String> fileNames = boardDTO.getFileNames();
        if(fileNames != null && fileNames.size() > 0){
            removeFiles(fileNames);
        }
        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/board/list";

    }

    public void removeFiles(List<String> files){

        for (String fileName:files) {

            Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
            String resourceName = resource.getFilename();

            try {
                String contentType = Files.probeContentType(resource.getFile().toPath());
                resource.getFile().delete();

                //섬네일이 존재한다면
                if (contentType.startsWith("image")) {
                    File thumbnailFile = new File(uploadPath + File.separator + "s_" + fileName);
                    thumbnailFile.delete();
                }

            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }//end for
    }
}
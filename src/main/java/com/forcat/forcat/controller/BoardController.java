package com.forcat.forcat.controller;

import com.forcat.forcat.dto.board.BoardDTO;
import com.forcat.forcat.dto.PageRequestDTO;
import com.forcat.forcat.dto.PageResponseDTO;
import com.forcat.forcat.dto.board.BoardListAllDTO;
import com.forcat.forcat.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

@Controller
@RequestMapping ("/board")
@Log4j2//로그 사용
@RequiredArgsConstructor//final, notnull 필드 생성자 자동 생성
public class BoardController {

    private final BoardService boardService;
    @Value ("${com.forcat.upload.path}")// import 시에 springframework으로 시작하는 Value
    private String uploadPath;

    @GetMapping ("/list") //게시글 목록 출력
    public void list (PageRequestDTO pageRequestDTO, Model model) {//pageRequestDTO를 이용해 페이징 처리 및 검색, model에 조회 결과를 추가하여 view로 전달
        log.info ("==========게시판 목록 출력");
        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll (pageRequestDTO);//boardService를 통해 게시글 목록을 조회하는 서비스 메서드인 listWithAll()을 호출
        model.addAttribute ("responseDTO", responseDTO);
    }

    @GetMapping ("/register")
    public void registerGET () {
        log.info ("==========게시판 등록 출력");
    }//게시글 등록

    @PostMapping ("/register")//게시글 등록
    public String registerPost (@Valid BoardDTO boardDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info ("==========게시판 등록 실행");
        Authentication authentication = SecurityContextHolder.getContext ().getAuthentication ();//현재 로그인 정보를 가져온다.
        String currentMemberId = authentication.getName ();//로그인 정보 중 아이디를 가져온다.
        boardDTO.setMid (currentMemberId);//로그인 아이디를 DTO에 입력한다.
        if (bindingResult.hasErrors ()) {
            log.info ("has errors.......");
            redirectAttributes.addFlashAttribute ("errors", bindingResult.getAllErrors ());
            return "redirect:/board/register";
        }
        log.info (boardDTO);
        Long bno = boardService.register (boardDTO);
        redirectAttributes.addFlashAttribute ("result", bno);
        return "redirect:/board/list";
    }


    @GetMapping ({"/read", "modify"})//게시글 조회, 수정
    public void read (Long bno, PageRequestDTO pageRequestDTO, Model model) {
        log.info ("==========게시글 조회 / 수정 출력");
        BoardDTO boardDTO = boardService.readOne (bno); //게시글 조회하기 위한 서비스 객체 생성
        model.addAttribute ("dto", boardDTO); //모델 객체에 dto라는 이름으로 boardDTO를 전달
    }

    @PreAuthorize("principal.username == #boardDTO.writer")
    @PostMapping ("/modify")//게시글 수정
    public String modify (@Valid BoardDTO boardDTO,//수정할 게시글 정보
                          BindingResult bindingResult,//유효성 검사
                          PageRequestDTO pageRequestDTO,//페이징
                          RedirectAttributes redirectAttributes) {//리다이텍트 시 데이터 전달
        log.info ("==========게시판 수정 실행" + boardDTO);
        if (bindingResult.hasErrors ()) {//유효성 오류 발생 시 처리
            log.info ("has errors.......");
            String link = pageRequestDTO.getLink ();//페이징 정보 link 저장
            redirectAttributes.addFlashAttribute ("errors", bindingResult.getAllErrors ());//리다이텍트 시 오류 정보 전달
            redirectAttributes.addAttribute ("bno", boardDTO.getBno ());//리다이렉트 시 수정할 게시글 번호 전달
            return "redirect:/board/modify?" + link;//리다이텍트하면서 오류 정보와 게시글 번호 전달
        }
        boardService.modify (boardDTO);//오류가 없는 경우 boardDTO 정보 사용하여 게시글 수정 서비스 호출
        redirectAttributes.addFlashAttribute ("result", "modified");//게시글 수정하면 result 속성 추가
        redirectAttributes.addAttribute ("bno", boardDTO.getBno ());//수정된 게시글 번호 전달
        return "redirect:/board/read";//게시글 수정이 완료되면 게시글 조회 페이지 리다이렉트
    }

    @PostMapping ("/remove")//게시글 삭제
    public String remove (BoardDTO boardDTO, RedirectAttributes redirectAttributes) {//게시글 정보를 가져와 플래시 메시지 전달
        Long bno = boardDTO.getBno ();//삭제할 게시글의 고유 번호를 가져옴
        log.info ("==========게시글 삭제 실행" + bno);
        boardService.remove (bno);//삭제 서비스 메서드 실행
        log.info (boardDTO.getFileNames ());
        List<String> fileNames = boardDTO.getFileNames (); //게시물이 삭제되었다면 첨부 파일 삭제
        if (fileNames != null && fileNames.size () > 0) {//파일 이름이 있는지 확인
            removeFiles (fileNames);//첨부 파일 삭제
        }
        redirectAttributes.addFlashAttribute ("result", "removed");
        return "redirect:/board/list";//게시글 삭제 후 게시글 목록으로 리다이렉트
    }

    public void removeFiles (List<String> files) {//삭제할 파일 이름 목록을 전달 받음
        log.info ("==========게시글 파일 삭제 실행");
        for (String fileName : files) {
            Resource resource = new FileSystemResource (uploadPath + File.separator + fileName);
            String resourceName = resource.getFilename ();
            try {
                String contentType = Files.probeContentType (resource.getFile ().toPath ());
                resource.getFile ().delete ();
                if (contentType.startsWith ("image")) {//썸네일이 존재한다면
                    File thumbnailFile = new File (uploadPath + File.separator + "s_" + fileName);
                    thumbnailFile.delete ();
                }
            } catch (Exception e) {
                log.error (e.getMessage ());
            }
        }//end for
    }
}

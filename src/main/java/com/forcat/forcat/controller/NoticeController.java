package com.forcat.forcat.controller;

import com.forcat.forcat.dto.notice.NoticeDTO;
import com.forcat.forcat.dto.notice.NoticeListAllDTO;
import com.forcat.forcat.dto.notice.NoticePageRequestDTO;
import com.forcat.forcat.dto.notice.NoticePageResponseDTO;
import com.forcat.forcat.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/notice")
@Log4j2
@RequiredArgsConstructor
public class NoticeController {

    @Value("${com.forcat.upload.path}")
    private String uploadPath;

    private final NoticeService noticeService;

    @GetMapping("/list")
    public void list(NoticePageRequestDTO noticePageRequestDTO, Model model) {

        NoticePageResponseDTO<NoticeListAllDTO> noticeResponseDTO = noticeService.noticeListWithAll(noticePageRequestDTO);

        log.info(noticeResponseDTO);

        model.addAttribute("noticeResponseDTO", noticeResponseDTO);

    } // list

    @PreAuthorize("hasRole('ADMIN')")//blog 페이지는 USER 권한 접속 가능
    @GetMapping("/register")
    public void registerGET() {}

    @PostMapping("/register")
    public String registerPost(@Valid NoticeDTO noticeDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        log.info("board POST register.......");

        if (bindingResult.hasErrors()) {

            log.info("has errors.......");

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());

            return "redirect:/notice/register";
        } // registerPost

        log.info(noticeDTO);

        Long noticeNo = noticeService.NoticeRegister(noticeDTO);

        redirectAttributes.addFlashAttribute("result", noticeNo);

        return "redirect:/notice/list";
    } // registerPost

    @GetMapping({"/read", "modify"})
    public void read(Long noticeNo, NoticePageRequestDTO noticePageRequestDTO, Model model) {

        //게시글 조회하기 위한 서비스 객체 생성
        NoticeDTO noticeDTO = noticeService.NoticeReadOne(noticeNo);

        log.info(noticeDTO);

        //모델 객체에 dto라는 이름으로 boardDTO를 전달
        model.addAttribute("noticeDTO", noticeDTO);

    } // read

    @PreAuthorize("hasRole('ADMIN')")//blog 페이지는 USER 권한 접속 가능
    @PostMapping("/modify")
    public String modify(@Valid NoticeDTO noticeDTO,//수정할 게시글 정보
                                BindingResult bindingResult,//유효성 검사
                                NoticePageRequestDTO noticePageRequestDTO,//페이징
                                RedirectAttributes redirectAttributes) {//리다이텍트 시 데이터 전달

        log.info("notice modify post......." + noticeDTO);

        if (bindingResult.hasErrors()) {//유효성 오류 발생 시 처리

            log.info("has errors.......");

            String noticeLink = noticePageRequestDTO.getNoticeLink();//페이징 정보 link 저장

            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            redirectAttributes.addAttribute("noticeNo", noticeDTO.getNoticeNo());

            return "redirect:/notice/modify?" + noticeLink;
        }

        noticeService.NoticeModify(noticeDTO);

        redirectAttributes.addFlashAttribute("result", "modified");//게시글 수정하면 result 속성 추가
        redirectAttributes.addAttribute("noticeNo", noticeDTO.getNoticeNo());//수정된 게시글 번호 전달

        return "redirect:/notice/read";//게시글 수정이 완료되면 게시글 조회 페이지 리다이렉트
    } // modify

    @PreAuthorize("hasRole('ADMIN')")//blog 페이지는 USER 권한 접속 가능
    @PostMapping("/remove")
    public String remove(NoticeDTO noticeDTO, RedirectAttributes redirectAttributes) {

        Long noticeNo = noticeDTO.getNoticeNo();

        log.info("noticeRemove post.. " + noticeNo);

        noticeService.NoticeRemove(noticeNo);

        //게시물이 삭제되었다면 첨부 파일 삭제
        log.info(noticeDTO.getFileNames());

        List<String> fileNames = noticeDTO.getFileNames();

        if (fileNames != null && fileNames.size() > 0) {

            removeFiles(fileNames);

        }

        redirectAttributes.addFlashAttribute("result", "removed");

        return "redirect:/notice/list";

    } // remove

    public void removeFiles(List<String> files) {

        for (String fileName : files) {

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

    } // removeFiles




} // NoticeController

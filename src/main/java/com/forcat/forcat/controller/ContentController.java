package com.forcat.forcat.controller;

import com.forcat.forcat.dto.content.ContentDTO;
import com.forcat.forcat.dto.content.ContentListAllDTO;
import com.forcat.forcat.dto.content.ContentPageRequestDTO;
import com.forcat.forcat.dto.content.ContentPageResponseDTO;
import com.forcat.forcat.dto.content.recent.ContentRListAllDTO;
import com.forcat.forcat.dto.content.recent.ContentRPageRequestDTO;
import com.forcat.forcat.dto.content.recent.ContentRPageResponseDTO;
import com.forcat.forcat.service.ContentService;
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

@Controller
@RequestMapping ("/content")
@Log4j2//로그 사용
@RequiredArgsConstructor//final, notnull 필드 생성자 자동 생성
public class ContentController {

    private final ContentService contentService;
    @Value ("${com.forcat.upload.path}")// import 시에 springframework으로 시작하는 Value
    private String uploadPath;

    @GetMapping ("/contentList")
    public void contentList (ContentPageRequestDTO contentPageRequestDTO, Model model) {
        ContentPageResponseDTO<ContentListAllDTO> contentResponseDTO = contentService.contentListWithAll (contentPageRequestDTO);
        log.info ("contentResponseDTO : " + contentResponseDTO);
        model.addAttribute ("contentResponseDTO", contentResponseDTO);
    } // list

    //@PreAuthorize("hasRole('ADMIN')")
    @GetMapping ("/contentRegister")
    public void contentRegisterGET () {
    } // registerGET

    @PostMapping ("/contentRegister")
    public String contentRegisterPost (@Valid ContentDTO contentDTO, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        log.info ("board POST register.......");

        if (bindingResult.hasErrors ()) {

            log.info ("has errors.......");

            redirectAttributes.addFlashAttribute ("errors", bindingResult.getAllErrors ());

            return "redirect:/content/contentRegister";

        } // registerPost

        log.info (contentDTO);

        Long cno = contentService.ContentRegister (contentDTO);

        redirectAttributes.addFlashAttribute ("result", cno);

        return "redirect:/content/contentList";

    } // registerPost

    /*게시글 조회, 수정*/
    @GetMapping ({"/contentRead", "contentModify"})
    public void contentRead (Long cno, ContentPageRequestDTO contentPageRequestDTO, ContentRPageRequestDTO contentRPageRequestDTO, Model model) {

        ContentRPageResponseDTO<ContentDTO> contentRResponseDTO = contentService.contentRList (contentRPageRequestDTO);

        //게시글 조회하기 위한 서비스 객체 생성
        ContentDTO contentDTO = contentService.ContentReadOne (cno);

        log.info (contentDTO);
        //모델 객체에 dto라는 이름으로 boardDTO를 전달
        model.addAttribute ("contentDto", contentDTO);
        model.addAttribute ("contentRResponseDTO", contentRResponseDTO);

        /*ContentPageResponseDTO<ContentListAllDTO> contentResponseDTO = contentService.contentListWithAll(contentPageRequestDTO);
        log.info(contentResponseDTO);
        model.addAttribute("contentResponseDTO", contentResponseDTO);*/

    } // read

    /*게시글 수정*/
    @PostMapping ("/contentModify")
    public String contentModify (@Valid ContentDTO contentDTO,//수정할 게시글 정보
                                 BindingResult bindingResult,//유효성 검사
                                 ContentPageRequestDTO contentpageRequestDTO,//페이징
                                 RedirectAttributes redirectAttributes) {//리다이텍트 시 데이터 전달
        log.info ("content modify post......." + contentDTO);

        if (bindingResult.hasErrors ()) {//유효성 오류 발생 시 처리

            log.info ("has errors.......");

            String cLink = contentpageRequestDTO.getCLink ();//페이징 정보 link 저장

            redirectAttributes.addFlashAttribute ("errors", bindingResult.getAllErrors ());//리다이텍트 시 오류 정보 전달
            redirectAttributes.addAttribute ("cno", contentDTO.getCno ());//리다이렉트 시 수정할 게시글 번호 전달

            return "redirect:/content/contentModify?" + cLink;//리다이텍트하면서 오류 정보와 게시글 번호 전달

        }

        contentService.contentModify (contentDTO);//오류가 없는 경우 boardDTO 정보 사용하여 게시글 수정 서비스 호출

        redirectAttributes.addFlashAttribute ("result", "modified");//게시글 수정하면 result 속성 추가
        redirectAttributes.addAttribute ("cno", contentDTO.getCno ());//수정된 게시글 번호 전달

        return "redirect:/content/contentRead";//게시글 수정이 완료되면 게시글 조회 페이지 리다이렉트

    } // modify

    /*게시글 삭제*/
    @PostMapping ("/contentRemove")
    public String contentRemove (ContentDTO contentDTO, RedirectAttributes redirectAttributes) {

        Long cno = contentDTO.getCno ();

        log.info ("contentRemove post.. " + cno);

        contentService.contentRemove (cno);

        //게시물이 삭제되었다면 첨부 파일 삭제
        log.info (contentDTO.getFileNames ());

        List<String> fileNames = contentDTO.getFileNames ();

        if (fileNames != null && fileNames.size () > 0) {

            removeFiles (fileNames);

        }

        redirectAttributes.addFlashAttribute ("result", "removed");

        return "redirect:/content/contentList";

    } // remove

    public void removeFiles (List<String> files) {

        for (String fileName : files) {

            Resource resource = new FileSystemResource (uploadPath + File.separator + fileName);

            String resourceName = resource.getFilename ();

            try {
                String contentType = Files.probeContentType (resource.getFile ().toPath ());

                resource.getFile ().delete ();

                //섬네일이 존재한다면
                if (contentType.startsWith ("image")) {

                    File thumbnailFile = new File (uploadPath + File.separator + "s_" + fileName);
                    thumbnailFile.delete ();

                }

            } catch (Exception e) {

                log.error (e.getMessage ());

            }
        }//end for

    } // removeFiles

} // ContentController

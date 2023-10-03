package com.forcat.forcat.controller;

import com.forcat.forcat.dto.upload.UploadFileDTO;
import com.forcat.forcat.dto.upload.UploadResultDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import net.coobird.thumbnailator.Thumbnailator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@Log4j2
public class UpDownController {

    @Value("${com.forcat.upload.path}")// import 시에 springframework으로 시작하는 Value
    private String uploadPath;//파일을 업로드하는 경로

    /*첨부파일 등록*/
    @ApiOperation(value = "Upload POST", notes = "POST 방식으로 파일 등록")//스웩 문서 생성
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)//파일 업로드 처리 메서드 명시
    public List<UploadResultDTO> upload(UploadFileDTO uploadFileDTO){//UploadFileDTO 객체를 값으로 받아 List<UploadResultDTO> 형태로 반환
        log.info(uploadFileDTO);

        if(uploadFileDTO.getFiles() != null) {//업로드 파일 목록이 비어있지 않은 경우에만 로직 실행

            final List<UploadResultDTO> list = new ArrayList<>();//업로드된 각 파일의 결과를 저장할 리스트 객체 생성

            uploadFileDTO.getFiles().forEach(multipartFile -> {//업로드된 파일은 files 필드에 저장, 각 파일을 multipartFile 변수 처리

                String originalName = multipartFile.getOriginalFilename();//원래 파일 이름을 얻음
                log.info(originalName);

                String uuid = UUID.randomUUID().toString();//파일 이름 중복을 피하기 위해 UUID 생성

                Path savePath = Paths.get(uploadPath, uuid + "_" + originalName);//업로드 파일 경로와 이름 설정

                boolean image = false;//이미지 파일 여부를 나타내는 변수를 초기화

                try {
                    multipartFile.transferTo(savePath);//업로드된 파일을 저장할 경로 설정

                    //이미지 파일의 종류라면 Thumbnailator를 사용하여 이미지를 생성
                    if(Files.probeContentType(savePath).startsWith("image")){
                        File thumbFile = new File(uploadPath, "s_" + uuid+"_"+ originalName);

                        image = true;

                        Thumbnailator.createThumbnail(savePath.toFile(), thumbFile, 200,200);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                list.add(UploadResultDTO.builder()//각 파일에 대한 결과를 UploadResultDTO 객체로 생성하고 결과 리스트에 추가
                        .uuid(uuid)
                        .fileName(originalName)
                        .img(image).build()
                );
            });//end each
            return list;
        }//end if
        return null;//업로드 처리 완료되면 null 반환
    }

    /*첨부파일 조회*/
    @ApiOperation(value = "view 파일", notes = "GET방식으로 첨부파일 조회")
    @GetMapping("/view/{fileName}")
    public ResponseEntity<Resource> viewFileGET(@PathVariable String fileName){
        //업로드된 파일을 조회하기 위해 파일 시스템에서 해당 파일을 찾아 Resource 객체로 생성
        Resource resource = new FileSystemResource(uploadPath+File.separator + fileName);
        String resourceName = resource.getFilename();//조회한 파일의 이름을 가져옴
        HttpHeaders headers = new HttpHeaders();

        try{//파일의 MIME 타입을 조회하여 HTTP 응답 헤더에 추가, 이렇게 함으로써 클라이언트가 어떤 형식의 파일인지 알 수 있음
            headers.add("Content-Type", Files.probeContentType( resource.getFile().toPath() ));
        } catch(Exception e){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    /*첨부파일 삭제*/
    @ApiOperation(value = "remove 파일", notes = "DELETE 방식으로 파일 삭제")
    @DeleteMapping("/remove/{fileName}")
    public Map<String,Boolean> removeFile(@PathVariable String fileName){
        //삭제할 파일을 찾기 위해 파일 시스템에서 해당 파일을 찾아 Resource 객체로 생성
        Resource resource = new FileSystemResource(uploadPath+File.separator + fileName);
        String resourceName = resource.getFilename();//조회한 파일의 이름을 가져옴

        Map<String, Boolean> resultMap = new HashMap<>();//결과를 저장할 맵을 생성, 삭제 작업의 성공 여부를 클라이언트에게 반환
        boolean removed = false;//파일 삭제 여부를 나타내는 변수

        try {//삭제할 파일의 MIME 타입을 조회, 이 정보를 기반으로 파일이 이미지인 경우, 해당 이미지 파일의 섬네일도 함께 삭제
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();//파일을 삭제

            //섬네일이 존재한다면 contentType가 "image"로 시작하는 경우, 해당 이미지의 섬네일 파일을 삭제
            if(contentType.startsWith("image")){
                File thumbnailFile = new File(uploadPath+File.separator +"s_" + fileName);
                thumbnailFile.delete();
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        resultMap.put("result", removed);//삭제 작업의 결과를 맵에 추가

        return resultMap;//삭제 작업 결과를 클라이언트에게 반환
    }
}

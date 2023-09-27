package com.forcat.forcat.controller;

import com.forcat.forcat.dto.upload.UploadFileDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
public class UpDownController {

    @ApiOperation(value = "Upload POST", notes = "POST 방식으로 파일 등록")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(UploadFileDTO uploadFileDTO){

        log.info(uploadFileDTO);
        return null;
    }
}

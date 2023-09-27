package com.forcat.forcat.dto.upload;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class UploadFileDTO {//파일 업로드 관련 클래스

    private List<MultipartFile> files;//클라이언트로부터 업로드된 파일의 내용과 메타데이터를 저장하는 데 사용
}


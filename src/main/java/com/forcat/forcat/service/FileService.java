package com.forcat.forcat.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log4j2
public class FileService {
//쇼핑몰 파일 첨부
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID(); // UUID : 네트워크상에서 고유성을 보장하는 ID를 만들기 위한 표준 규약
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        // substring : 인덱스부터 끝까지 문자열 추출
        // lastIndexOf : 문자열 뒤에서부터 문자를 찾아 인덱스 반환
        // 원본 파일명에서 확장자(.이후)를 추출하여 extension으로 저장
        String savedFileName = uuid + extension;
        // UUID.확장자의 형태로 새로운 파일명을 만들어 savedFileName에 저장
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        // 업로드 경로/새로운 파일명을 조합하여 전체 파일 경로 fileUploadFullUrl로 저장
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        // 파일 출력 스트림 생성하여 저장할 경로와 연결
        fos.write(fileData); // 파일 출력 스트림을 통해 파일 데이터 저장
        fos.close(); // 파일 출력 스트림 닫기
        return savedFileName; // 저장된 새로운 파일명을 리턴
    }

    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath); // 삭제할 파일 경로를 이용하여 File 객체 생성
        if(deleteFile.exists()) { // 삭제 파일이 존재한다면
            deleteFile.delete(); // 파일 삭제
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}

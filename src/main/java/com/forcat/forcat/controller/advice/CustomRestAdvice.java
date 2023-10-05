package com.forcat.forcat.controller.advice;

import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice//컨트롤러가 적용된 Bean 발생하는 예외를 잡아 메서드에서 처리
@Log4j2//로그 출력
public class CustomRestAdvice {//REST 방식은 눈에 보이지 않는 방식으로 서버 사용하다보니 원인을 모를 수 있어서 유효성에 문제가 생기면 처리할 수 있도록 설계하는 클래스

    @ExceptionHandler(BindException.class)//BindException 예외처리
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)//응답 상태 코드, 클아이언트 요청 예상 실패 처리됨
    public ResponseEntity<Map<String, String>> handleBindException(BindException e) {//예외처리 핸들러 메서드 정의, BindException를 변수로 받음
        log.info("==========바인딩 예외");
        log.error(e);
        Map<String, String> errorMap = new HashMap<>();//객체 생성
        if(e.hasErrors()){//객체에 에러 확인
            BindingResult bindingResult = e.getBindingResult();
            bindingResult.getFieldErrors().forEach(fieldError -> {//각 필드 에러 정보 반복 찾기, 필드 이름과 코드 추출
                errorMap.put(fieldError.getField(), fieldError.getCode());
            });
        }
        return ResponseEntity.badRequest().body(errorMap);//errorMap을 HTTP 응답으로 반환, 상태 코드 400 반환, JSON 형식 반환
    }

    @ExceptionHandler(DataIntegrityViolationException.class)//데이터 무결성 위반 예외 발생 시 예외 처리
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)//응답 상태 코드, 클아이언트 요청 예상 실패 처리됨
    public ResponseEntity<Map<String, String>> handleFKException(Exception e) {//데이터 무결성 위반 예외 처리하고 응답 생성
        log.info("==========데이터 무결성 위반");
        log.error(e);
        Map<String, String> errorMap = new HashMap<>();//Map<String, String>형태의 오류 메세지 생성
        errorMap.put("time", ""+System.currentTimeMillis());//시간 표시
        errorMap.put("msg",  "constraint fails");//오류 메세지 표시
        return ResponseEntity.badRequest().body(errorMap);//HTTP 응답, 상태코드 400, errorMap 반환
    }

    //댓글 데이터 존재하지 않는 경우 처리
    @ExceptionHandler({NoSuchElementException.class, EmptyResultDataAccessException.class})//데이터 무결성 위반, 존재하지 않는 번호 예외 발생 시 처리
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)//응답 상태 코드, 클아이언트 요청 예상 실패 처리됨
    public ResponseEntity<Map<String, String>> handleNoSuchElement(Exception e) {//데이터 무결성 위반 예외 처리하고 응답 생성
        log.info("==========댓글 데이터 무결성 위반");
        log.error(e);
        Map<String, String> errorMap = new HashMap<>();//Map<String, String>형태의 오류 메세지 생성
        errorMap.put("time", ""+System.currentTimeMillis());
        errorMap.put("msg",  "No Such Element Exception");
        return ResponseEntity.badRequest().body(errorMap);
    }
}

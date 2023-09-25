package com.forcat.forcat.service;

import com.forcat.forcat.dto.MemberJoinDTO;

public interface MemberService {

    //이미지 존재하는 ID면 예외 처리
    static class MidExistException extends Exception {
    }

    void join(MemberJoinDTO memberJoinDTO)throws MidExistException ;

}

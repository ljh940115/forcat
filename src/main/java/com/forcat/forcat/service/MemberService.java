package com.forcat.forcat.service;

import com.forcat.forcat.dto.MemberJoinDTO;
import com.forcat.forcat.entity.Member;

public interface MemberService {
    static class member_idExistException extends Exception {

    }

    //회원가입
    void join(MemberJoinDTO memberJoinDTO)throws member_idExistException ;
}

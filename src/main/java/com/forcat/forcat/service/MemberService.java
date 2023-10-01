package com.forcat.forcat.service;

import com.forcat.forcat.dto.MemberJoinDTO;

public interface MemberService {
    static class MidExistException extends Exception {

    }

    void join(MemberJoinDTO memberJoinDTO)throws MidExistException ;

}

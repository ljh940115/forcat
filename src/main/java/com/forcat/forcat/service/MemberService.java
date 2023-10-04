package com.forcat.forcat.service;

import com.forcat.forcat.dto.MemberJoinDTO;

public interface MemberService {
    static class member_idExistException extends Exception {

    }

    void join(MemberJoinDTO memberJoinDTO)throws member_idExistException ;

}

package com.forcat.forcat.service;

import com.forcat.forcat.dto.MemberJoinDTO;
import com.forcat.forcat.dto.MemberUpdateDTO;

public interface MemberService {
    static class MidExistException extends Exception { }

    void join(MemberJoinDTO memberJoinDTO)throws MidExistException ;

    void update(MemberUpdateDTO memberUpdateDTO)throws MidExistException ;

    void delete(String mid);
}

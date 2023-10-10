package com.forcat.forcat.service;

import com.forcat.forcat.dto.member.MemberCheckDTO;
import com.forcat.forcat.dto.member.MemberJoinDTO;
import com.forcat.forcat.dto.member.MemberUpdateDTO;

public interface MemberService {

    void check (MemberCheckDTO memberCheckDTO) throws MidExistException;

    void join (MemberJoinDTO memberJoinDTO) throws MidExistException;

    void update (MemberUpdateDTO memberUpdateDTO) throws MidExistException;

    void delete (String mid);

    class MidExistException extends Exception {
    }
}

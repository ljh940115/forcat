package com.forcat.forcat.dto.member;

import lombok.Data;

@Data
public class MemberJoinDTO {//회원가입 DTO, 일반적으로 회원가입은 단순 USER이므로 별도 화면 구성 X

    private String mid;
    private String mpw;
    private String name;//회원이름
    private String email;//회원 이메일
    private String address;//회원 주소
    private boolean del;
    private boolean social;
}

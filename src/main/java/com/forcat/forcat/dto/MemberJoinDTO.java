package com.forcat.forcat.dto;

import lombok.Data;

@Data
public class MemberJoinDTO {//회원가입 DTO, 일반적으로 회원가입은 단순 USER이므로 별도 화면 구성 X

    private String mid;
    private String mpw;
    private String email;
    private boolean del;
    private boolean social;

}

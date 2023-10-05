package com.forcat.forcat.dto.member;

import lombok.Data;

@Data
public class MemberUpdateDTO {//회원 수정 DTO

    private String mid;
    private String mpw;
    private String email;
}

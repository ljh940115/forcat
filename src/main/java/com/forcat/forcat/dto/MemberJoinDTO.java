package com.forcat.forcat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberJoinDTO {//회원가입 DTO, 일반적으로 회원가입은 단순 USER이므로 별도 화면 구성 X

    private String member_id;
    private String member_pw;
    private String email;
    private String name;
    private String address;
    private boolean del;
    private boolean social;
}


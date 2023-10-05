package com.forcat.forcat.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberFormDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    // NotBlank : Null값 비허용 ("", " " 모두 비허용)
    private String name;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Length(min=8, max=16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요")
    // @Length : 필드 크기 검증
    private String password;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address;
}

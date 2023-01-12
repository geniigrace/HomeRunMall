package com.shop.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberFormDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String userID;

    @NotBlank(message = "전화번호를 입력해주세요.")
    @Length(min=7, max=11, message = "하이픈을 제외하고 입력해주세요.(010 포함)")
    private String phone;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Length(min=6, max=16, message = "비밀번호는 6자 이상, 16자 이하입니다.")
    private String password;

    private String address;

}
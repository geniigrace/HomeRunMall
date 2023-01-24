package com.baseballshop.dto;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class MemberFormDto {

    @NotEmpty(message = "구단 선택은 필수 입니다.")
    private String userTeam;

    @NotBlank(message = "아이디는 필수 입력 항목입니다.")
    @Length(min=2, max=10, message = "아이디는 2자 이상, 10자 이하로 입력하세요.")
    private String userId;

    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
    @Length(min=8, max=16, message = "비밀번호는 8자 이상, 16자 이하로 입력하세요")
    private String password;

    @NotEmpty(message = "비밀번호를 한 번 더 입력하세요.")
    private String passwordCheck;

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String name;

    @NotBlank(message = "휴대폰번호는 필수 입력 항목입니다.")
    @Length(min=11, max=11, message = "하이픈('-')을 제외한 11자리로 입력하세요.")
    private String phone;

    @NotEmpty(message = "주소는 필수 입력 항목입니다.")
    private String address;


}

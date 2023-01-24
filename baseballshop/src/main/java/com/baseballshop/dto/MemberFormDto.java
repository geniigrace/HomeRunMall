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
    private String memberTeam;

    @NotBlank(message = "아이디는 필수 입력 항목입니다.")
    @Length(min=2, max=10, message = "아이디는 2자 이상, 10자 이하로 입력하세요.")
    private String memberId;

    @NotEmpty(message = "비밀번호는 필수 입력 항목입니다.")
    @Length(min=8, max=16, message = "비밀번호는 8자 이상, 16자 이하로 입력하세요")
    private String memberPw;

    @NotEmpty(message = "비밀번호를 한 번 더 입력하세요.")
    private String memberPwCheck;

    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String memberEmail;

    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String memberName;

    @NotBlank(message = "생년월일은 필수 업릭 항목입니다.")
    private String memberBirth;

    @NotBlank(message = "휴대폰번호는 필수 입력 항목입니다.")
    private String memberPhone;

    @NotEmpty(message = "주소는 필수 입력 항목입니다.")
    private String memberAddr;


}

package com.baseballshop.dto;


import com.baseballshop.constant.Team;
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

    @NotBlank(message = "아이디를 입력하세요.")
    @Length(min=2, max=10, message = "아이디는 2자 이상, 10자 이하로 입력하세요.")
    private String userId;

    @NotEmpty(message = "비밀번호를 입력하세요.")
    @Length(min=8, max=16, message = "비밀번호는 8자 이상, 16자 이하로 입력하세요")
    private String password;

    @NotEmpty(message = "비밀번호를 한 번 더 입력하세요.")
    private String passwordCheck;

    @NotBlank(message = "본인확인시 사용할 이메일 주소를 입력하세요.")
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String email;

    @NotBlank(message = "이름을 입력하세요.")
    private String name;

    @NotBlank(message = "연락처를 입력하세요.")
    @Length(min=11, max=11, message = "하이픈('-')을 제외한 11자리로 입력하세요.")
    private String phone;

    @NotEmpty(message = "주소를 입력하세요.")
    private String address;


}

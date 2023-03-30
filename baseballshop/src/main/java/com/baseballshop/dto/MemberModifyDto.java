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
public class MemberModifyDto {


    private Team team;

    private String email;

    @NotEmpty(message = "비밀번호를 입력하세요.")
    @Length(min=8, max=16, message = "비밀번호는 8자 이상, 16자 이하로 입력하세요")
    private String password;

    @NotEmpty(message = "비밀번호를 한 번 더 입력하세요.")
    private String passwordCheck;

    @NotBlank(message = "이름을 입력하세요.")
    private String name;

    @NotBlank(message = "연락처를 입력하세요.")
    @Length(min=11, max=11, message = "하이픈('-')을 제외한 11자리로 입력하세요.")
    private String phone;

    @NotEmpty(message = "주소를 입력하세요.")
    private String address1;

    @NotEmpty(message = "상세 주소를 입력하세요.")
    private String address2;

    //true : sns, false : local
    private boolean loginRoot;

}

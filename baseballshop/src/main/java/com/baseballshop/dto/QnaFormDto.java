package com.baseballshop.dto;

import com.baseballshop.constant.NoticeStatus;
import com.baseballshop.constant.QnaStatus;
import com.baseballshop.constant.ShowStatus;
import com.baseballshop.entity.Notice;
import com.baseballshop.entity.Qna;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class QnaFormDto {

    private Long id;

    private ShowStatus showStatus;

    //문의사항 카테고리
    private QnaStatus qnaType;

    //답변상태 카테고리
    private QnaStatus answerType;

    private String qnaDate;

    private String qnaEmail;

    @NotBlank(message = "제목을 입력하세요.")
    private String qnaTitle;

    @NotBlank(message = "내용을 입력하세요.")
    private String qnaContent;

}

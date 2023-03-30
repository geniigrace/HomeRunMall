package com.baseballshop.dto;

import com.baseballshop.constant.QnaStatus;
import com.baseballshop.constant.ShowStatus;
import com.baseballshop.entity.Qna;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class QnaListDto {

    private Long id;

    private ShowStatus showStatus;

    //문의사항 카테고리
    private QnaStatus qnaType;

    //답변상태 카테고리
    private QnaStatus answerType;

    private LocalDateTime qnaDate;

    @NotBlank(message = "제목을 입력하세요.")
    private String qnaTitle;

    @NotBlank(message = "내용을 입력하세요.")
    private String qnaContent;

    private String qnaMemberId;

    private String qnaEmail;

    public QnaListDto(Qna qna){
        this.id=qna.getId();
        this.showStatus=qna.getShowStatus();
        this.qnaType=qna.getQnaType();
        this.qnaDate=qna.getCreateTime();
        this.answerType=qna.getAnswerType();
        this.qnaTitle=qna.getQnaTitle();
        this.qnaContent=qna.getQnaContent();
        this.qnaMemberId=qna.getMember().getEmail().split("@")[0];
        this.qnaEmail=qna.getMember().getEmail();
    }
}

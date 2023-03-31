package com.baseballshop.entity;

import com.baseballshop.constant.QnaStatus;
import com.baseballshop.constant.ShowStatus;
import com.baseballshop.dto.QnaFormDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="qna")
@Getter
@Setter
@ToString
public class Qna extends BaseTimeEntity{

    @Id
    @Column(name="qna_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private ShowStatus showStatus;

    @Enumerated(EnumType.STRING)
    private QnaStatus qnaType;

    @Enumerated(EnumType.STRING)
    private QnaStatus answerType;

    @Column(name="qna_title", nullable = false)
    private String qnaTitle;

    @Lob
    @Column(name="qna_content", nullable = false)
    private String qnaContent;

//    @OrderBy("id desc")
//    @JsonIgnoreProperties({"qna"})
//    @OneToMany(mappedBy = "qna", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
//    private List<QnaReply> qnaReplyList;

    public static Qna createQna(Member member, QnaFormDto qnaFormDto){
        Qna qna=new Qna();
        qna.setMember(member);
        qna.setShowStatus(qnaFormDto.getShowStatus());
        qna.setQnaType(qnaFormDto.getQnaType());
        qna.setAnswerType(QnaStatus.NOT);
        qna.setQnaTitle(qnaFormDto.getQnaTitle());
        qna.setQnaContent(qnaFormDto.getQnaContent());

        return qna;
    }

    public void updateQna(QnaFormDto qnaFormDto){
        this.qnaTitle = qnaFormDto.getQnaTitle();
        this.qnaContent = qnaFormDto.getQnaContent();
        this.qnaType=qnaFormDto.getQnaType();
    }

}

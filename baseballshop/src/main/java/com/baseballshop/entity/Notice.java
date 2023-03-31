package com.baseballshop.entity;

import com.baseballshop.constant.NoticeStatus;
import com.baseballshop.constant.ShowStatus;
import com.baseballshop.constant.Team;
import com.baseballshop.dto.NoticeFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="notice")
@Getter
@Setter
@ToString
public class Notice extends BaseEntity{

    @Id
    @Column(name="notice_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //번호

    @Enumerated(EnumType.STRING)
    private ShowStatus showStatus;

    @Column(name="notice_title", nullable = false)
    private String noticeTitle;

    @Lob
    @Column(name="notice_content", nullable = false)
    private String noticeContent;

    @Enumerated(EnumType.STRING)
    private NoticeStatus noticeType;

    @Enumerated(EnumType.STRING)
    private Team team;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<NoticeImg> noticeImgs ;


    //공지 수정
    public void updateNotice(NoticeFormDto noticeFormDto){
        this.noticeTitle = noticeFormDto.getNoticeTitle();
        this.noticeContent = noticeFormDto.getNoticeContent();
        this.noticeType=noticeFormDto.getNoticeType();
        this.team = noticeFormDto.getTeam();
    }


}
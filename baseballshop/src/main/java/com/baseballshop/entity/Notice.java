package com.baseballshop.entity;

import com.baseballshop.constant.NoticeStatus;
import com.baseballshop.constant.ShowStatus;
import com.baseballshop.constant.Team;
import com.baseballshop.dto.NoticeFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

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
    private NoticeStatus noticeStatus;

    @Enumerated(EnumType.STRING)
    private Team team;


    //공지 수정
    public void updateNotice(NoticeFormDto noticeFormDto){
        this.noticeTitle = noticeFormDto.getNoticeTitle();
        this.noticeContent = noticeFormDto.getNoticeContent();
        this.noticeStatus=noticeFormDto.getNoticeStatus();
        this.team = noticeFormDto.getTeam();
    }


}
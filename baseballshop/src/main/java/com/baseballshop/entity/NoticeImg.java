package com.baseballshop.entity;

import com.baseballshop.constant.ShowStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="notice_img")
@Getter
@Setter
public class NoticeImg  extends  BaseEntity{

    @Id
    @Column(name = "notice_img_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private ShowStatus showStatus;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    //공지 수정
    public void updateNoticeImg(String oriImgName, String imgName, String imgUrl){
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
package com.baseballshop.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@EntityListeners(value = {AuditingEntityListener.class}) //auditing을 하기 위해 엔티티 리스너를 추가함
@MappedSuperclass //부모클래스를 상속받는 자식 클래스에 맵핑정보만 제공
@Getter
@Setter
public abstract class BaseTimeEntity {

    @CreatedDate //생성시 자동저장
    @Column(updatable = false) //생성이후 수정할 수 없게 설정
    private LocalDateTime regTime;

    @CreatedDate
    @Column(updatable = false)
    private String createTime;

    @LastModifiedDate
    private String updateTime;

    @PrePersist
    public void onPrePersist(){
        this.createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_hh:mm:ss"));
        this.updateTime = this.createTime;
    }

}

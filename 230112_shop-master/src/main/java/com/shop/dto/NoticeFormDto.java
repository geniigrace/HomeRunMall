package com.shop.dto;

import com.shop.constant.NoticeStatus;
import com.shop.entity.Notice;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class NoticeFormDto {

    private Long id;

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String noticeTitle;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String noticeContent;

    //공지사항 카테고리 (공지사항_NOTICE, 이벤트_EVENT)
    private NoticeStatus noticeStatus;

    private String noticeDate;

    private List<NoticeImgDto> noticeImgDtoList = new ArrayList<>();

    private List<Long> noticeImgIds = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();


    public Notice createNotice() {
        return modelMapper.map(this, Notice.class);
    }

    public static NoticeFormDto of(Notice notice){

        return modelMapper.map(notice, NoticeFormDto.class);
    }

}

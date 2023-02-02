package com.baseballshop.dto;

import com.baseballshop.constant.NoticeStatus;
import com.baseballshop.constant.ShowStatus;
import com.baseballshop.entity.Notice;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NoticeFormDto {

    private Long id;

    private ShowStatus showStatus;

    @NotBlank(message = "제목을 입력하세요.")
    private String noticeTitle;

    @NotBlank(message = "내용을 입력하세요.")
    private String noticeContent;

    //공지사항 카테고리 (공지사항_NOTICE, 이벤트_EVENT)
    private NoticeStatus noticeStatus;

    @NotNull(message = "구단을 선택하세요.")
    private String noticeTeam;

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

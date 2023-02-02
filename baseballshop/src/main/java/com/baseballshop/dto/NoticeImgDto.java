package com.baseballshop.dto;

import com.baseballshop.constant.ShowStatus;
import com.baseballshop.entity.NoticeImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class NoticeImgDto {

    private Long id;

    private ShowStatus showStatus;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private static ModelMapper modelMapper = new ModelMapper();

    public static NoticeImgDto of(NoticeImg noticeImg){
        return modelMapper.map(noticeImg, NoticeImgDto.class);
    }

}

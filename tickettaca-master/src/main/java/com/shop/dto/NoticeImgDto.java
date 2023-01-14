package com.shop.dto;

import com.shop.entity.NoticeImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class NoticeImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private static ModelMapper modelMapper = new ModelMapper();

    public static NoticeImgDto of(NoticeImg noticeImg){
        return modelMapper.map(noticeImg, NoticeImgDto.class);
    }

}

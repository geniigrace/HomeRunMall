package com.shop.service;


import com.shop.entity.NoticeImg;
import com.shop.repository.NoticeImgRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeImgService {

    @Value("${imgLocation}") // 기존 itemLocation 과 중복되어 공용변수로 변경하였음
    private String noticeImgLocation;

    private final NoticeImgRepository noticeImgRepository;

    private final FileService fileService;

    public void saveNoticeImg(NoticeImg noticeImg, MultipartFile noticeImgFile) throws  Exception {
        String oriImgName = noticeImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";


        if(!StringUtils.isEmpty(oriImgName)){
            imgName = fileService.uploadFile(noticeImgLocation, oriImgName, noticeImgFile.getBytes());
            imgUrl="/images/item/"+imgName;

            noticeImg.updateNoticeImg(oriImgName, imgName, imgUrl);
            noticeImgRepository.save(noticeImg);
        }
    }


    public void updateNoticeImg(Long noticeImgId, MultipartFile noticeImgFile) throws Exception{
        if(!noticeImgFile.isEmpty()){
            NoticeImg savedNoticeImg = noticeImgRepository.findById(noticeImgId).orElseThrow(EntityNotFoundException::new);

            if(!StringUtils.isEmpty(savedNoticeImg.getImgName())){
                fileService.deleteFile(noticeImgLocation + "/" + savedNoticeImg.getImgName());
            }

            String oriImgName = noticeImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(noticeImgLocation, oriImgName, noticeImgFile.getBytes());
            String imgUrl = "/images/notice/" + imgName;

            savedNoticeImg.updateNoticeImg(oriImgName, imgName, imgUrl);
        }

    }

}

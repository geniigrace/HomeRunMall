package com.baseballshop.service;


import com.baseballshop.constant.ShowStatus;
import com.baseballshop.dto.*;
import com.baseballshop.entity.Notice;
import com.baseballshop.entity.NoticeImg;
import com.baseballshop.repository.NoticeImgRepository;
import com.baseballshop.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final NoticeImgRepository noticeImgRepository;
    private final NoticeImgService noticeImgService;

    //공지사항 리스트 페이지
    @Transactional(readOnly = true)
    public Page<Notice> getNoticePage(NoticeSearchDto noticeSearchDto, Pageable pageable){
        return noticeRepository.getNoticePage(noticeSearchDto, pageable);
    }

    //공지사항 등록
    public Long saveNotice(NoticeFormDto noticeFormDto, List<MultipartFile> noticeImgFileList) throws Exception {

        Notice notice = noticeFormDto.createNotice();
        noticeRepository.save(notice);

        for(int i=0; i<noticeImgFileList.size(); i++){

            NoticeImg noticeImg = new NoticeImg();
            noticeImg.setNotice(notice);

            noticeImgService.saveNoticeImg(noticeImg, noticeImgFileList.get(i));
        }

        return notice.getId();

    }

    // 공지사항 수정 : 수정전 내용 출력
    @Transactional(readOnly = true)
    public NoticeFormDto preNotice(Long noticeId){

        //이미지 정보 받아오기
        List<NoticeImg> noticeImgList = noticeImgRepository.findByNoticeIdOrderByIdAsc(noticeId);
        List<NoticeImgDto> noticeImgDtoList = new ArrayList<>();

        for(NoticeImg noticeimg : noticeImgList){
            NoticeImgDto noticeImgDto = NoticeImgDto.of(noticeimg);
            noticeImgDtoList.add(noticeImgDto);
        }

        Notice notice = noticeRepository.findById(noticeId).orElseThrow(EntityNotFoundException::new);
        NoticeFormDto noticeFormDto = NoticeFormDto.of(notice);
        noticeFormDto.setNoticeImgDtoList(noticeImgDtoList);

        return noticeFormDto;
    }

    //공지사항 수정 : 수정한 내용 저장하기
    public Long updateNotice(NoticeFormDto noticeFormDto, List<MultipartFile> noticeImgFileList) throws Exception{
        Notice notice = noticeRepository.findById(noticeFormDto.getId()).orElseThrow(EntityNotFoundException::new);
        notice.updateNotice(noticeFormDto);

        List<Long> noticeImgIds = noticeFormDto.getNoticeImgIds();

        for(int i=0; i<noticeImgFileList.size(); i++){
            noticeImgService.updateNoticeImg(noticeImgIds.get(i), noticeImgFileList.get(i));

        }
        return notice.getId();
    }

    //공지사항 삭제
//

    public Long modify(NoticeDeleteDto noticeDeleteDto){

        List<NoticeDeleteDto> noticeDeleteDtoList = noticeDeleteDto.getNoticeDeleteDtoList();

        Long nid=noticeDeleteDtoList.get(0).getNoticeId();

        for(NoticeDeleteDto noticeDelete : noticeDeleteDtoList) {

            Long noticeId = noticeDelete.getNoticeId();

            Notice notice = noticeRepository.findById(noticeId).orElseThrow(EntityExistsException::new);

            notice.setShowStatus(ShowStatus.HIDE);

        }

        return nid;

    }


}

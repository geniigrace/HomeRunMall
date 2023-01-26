package com.baseballshop.service;

import com.baseballshop.entity.ItemImg;
import com.baseballshop.repository.ItemImgRepository;
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
public class ItemImgService {

    @Value("${imgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    //상품등록 : 파일 저장을 위한 서비스 생성
    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{

        //oriImgName : 상품 이미지 파일의 원래 이름
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";
        System.out.println(oriImgName);

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){
            //imgName : 실제 로컬에 저장된 상품 이미지 파일 이름
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            System.out.println(imgName);
            //imgUrl : 로컬에 저장된 상품 이미지 파일 불러오는 경로
            imgUrl = "/images/item/" + imgName;

        }
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg);

    }


    // 상품 파일 수정을 위한 추가
    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{
        if(!itemImgFile.isEmpty()){ //상품의 이미지를 수정한 경우 상품 이미지 업데이트
            //기존 엔티티 조회
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new);

            // 기존에 등록된 상품 이미지 파일이 있는 경우 파일 삭제하기
            if(!StringUtils.isEmpty(savedItemImg.getImgName())){
                fileService.deleteFile(itemImgLocation + "/" + savedItemImg.getImgName());
            }

            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;
            //변경된 상품 이미지 정보를 세팅
            //상품등록을 하지 않는 경우에는 ItemImgRepository.save()로 호출하지만 호출하지 않음
            //savedItemImg 엔티티는 현재 영속성 상태이다.
            //따라서 데이터를 변경하는 것만으로도 변경을 감지하는 기능이 동작하며 트랜잭션이 끝날때 Update 쿼리가 실행된다.

            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
        }

    }
}


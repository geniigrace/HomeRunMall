package com.baseballshop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{

        //랜덤으로 Uuid 생성 : 파일명을 위한 랜덤
        UUID uuid = UUID.randomUUID();

        //확장자 추출
        String extension = originalFileName.substring((originalFileName.lastIndexOf(".")));

        //랜덤이름+확장자
        String savedFileName = uuid.toString() + extension;


        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        System.out.println(fileUploadFullUrl);

        //이름전환한 파일의 전체 url을 파일써줌
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();

        return savedFileName; //경로없이 변환된 이름과 확장자
    }

    public void deleteFile(String filePath) throws Exception{

        File deleteFile = new File(filePath);

        if(deleteFile.exists()){
            deleteFile.delete();
            log.info("파일을 삭제했습니다.");
        }
        else {
            log.info("파일이 존재하지 않습니다.");
        }
    }
}


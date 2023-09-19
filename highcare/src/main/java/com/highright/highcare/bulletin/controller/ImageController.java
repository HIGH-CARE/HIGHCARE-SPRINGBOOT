package com.highright.highcare.bulletin.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.Random;

@RestController
public class ImageController {

    @Value("${image.image-dir}")
    String UPLOAD_PATH; // src/main/resources/static/images


    @Value("${image.image-url}")
    String URL_PATH; //http://highcare.coffit.today:8080/images/


    @Operation(summary = "이미지 조회", description = "이미지를 조회합니다", tags = {"ImageController"})
    @GetMapping("/getImage/{fileId}/{fileType}")
    public ResponseEntity<byte[]> getImageFile(@PathVariable String fileId,@PathVariable String fileType){
        try {
            FileInputStream fis = new FileInputStream(UPLOAD_PATH + "\\" + fileId + "." + fileType);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte buffer[] = new byte[1024];
            int length = 0;

            while((length = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }

            return new ResponseEntity<byte[]>(baos.toByteArray(), HttpStatus.OK);

        } catch(IOException e) {
            return new ResponseEntity<byte[]>(new byte[] {}, HttpStatus.CONFLICT);
        }
    }
    @Operation(summary = "이미지 등록", description = "이미지를 등록합니다", tags = {"ImageController"})
    @PostMapping("/uploadImage")
    public ResponseEntity<Object> uploadImage(MultipartFile multipartFiles[]) {
        System.out.println(multipartFiles[0]);
        MultipartFile file = multipartFiles[0];


        String fileId = (new Date().getTime()) + "" + (new Random().ints(1000, 9999).findAny().getAsInt()); // 현재 날짜와 랜덤 정수값으로 새로운 파일명 만들기
        String originName = file.getOriginalFilename(); // ex) 파일.jpg
        String fileExtension = originName.substring(originName.lastIndexOf(".") + 1); // ex) jpg
        originName = originName.substring(0, originName.lastIndexOf(".")); // ex)
        System.out.println("originName : " + originName);
        Path uploadPath = Paths.get(UPLOAD_PATH);

        try (InputStream inputStream = file.getInputStream()){
            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            String replaceFileName = fileId + "." + FilenameUtils.getExtension(file.getResource().getFilename());
            Path filePath = uploadPath.resolve(replaceFileName);
            Files.copy(inputStream, filePath,  StandardCopyOption.REPLACE_EXISTING);

            long fileSize = file.getSize();

            File fileSave = new File(UPLOAD_PATH, fileId + "." +fileExtension);


            return new ResponseEntity<Object>( "http://highcare.coffit.today:8080/getImage/"+ fileId + "/" + fileExtension, HttpStatus.OK);
        } catch(IOException e) {

            return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
        }
    }


}

package com.highright.highcare.auth.dto;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MailDTO {
    private String name;
    private String mail;        // 메일 주소
    private String title;
    private String message;
    private MultipartFile file;


}
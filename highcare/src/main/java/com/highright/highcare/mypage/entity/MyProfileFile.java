package com.highright.highcare.mypage.entity;

import lombok.*;
import org.springframework.context.annotation.Profile;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name="TBL_MY_PROFILE_FILE")
@Getter
@Setter
@SequenceGenerator(
        name = "MYFILE_SEQ_NO",
        sequenceName = "FILE_SEQ_NO",
        initialValue = 1, allocationSize = 1
)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MyProfileFile {

    @Id
    @Column (name = "PHOTO_FILE_CODE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MYFILE_SEQ_NO")
    private int photoCode;

    @Column(name="PROFILE_CODE")
    private int code;

    @Column(name = "ORIGINAL_FILE_NAME")
    private String name;

    @Column(name = "CHANGED_FILE_NAME")
    private String chName;

    @Column(name="PROFILE_IMAGE_URL")
    private String profileImgUrl;

    @Column(name = "CREATION_DATE" ,nullable = true)
    private Date date;


    @Builder
    public MyProfileFile(int code, String name, String chName, String profileImgUrl, Date date) {
        this.code = code;
        this.name = name;
        this.chName = chName;
        this.profileImgUrl = profileImgUrl;
        this.date = date;
    }
}

package com.highright.highcare.approval.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "TBL_APV_FILE")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@SequenceGenerator(
        name = "SEQ_APV_FILE",
        sequenceName = "SEQ_APV_FILE",
        initialValue = 1, allocationSize = 1
)
public class ApvFile {

    @Id
    @Column(name = "FILE_NO")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "SEQ_APV_FILE"
    )
    private Long fileNo;


    @Column(name = "ORIGINAL_FILE_NAME")
    private String originalFileName;

    @Column(name = "SAVED_FILE_NAME")
    private String savedFileName;

    @Column(name = "FILE_URL")
    private String fileUrl;


    @Column(name = "APV_NO")
    private Long apvNo;

}

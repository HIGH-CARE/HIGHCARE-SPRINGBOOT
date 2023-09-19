package com.highright.highcare.reservation.entity;

import lombok.*;
import org.checkerframework.checker.units.qual.C;

import javax.persistence.*;

@Entity
@Table(name = "TBL_RESOURCE_FILE")
@SequenceGenerator(
        name = "RES_SEQ_GENERATOR",
        sequenceName = "SEQ_RES_FILE_CODE",
        initialValue = 1, allocationSize = 1
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ResourceFile {
    @Id
    @Column(name = "FILE_CODE")
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "RES_SEQ_GENERATOR"
    )
    private int fileCode;

    @Column(name = "TYPE")
    private String type;
    @Column(name = "ORIGINAL_FILE_NAME")
    private String originalFileName;
    @Column(name = "CHANGED_FILE_NAME")
    private String changedFileName;
    @Column(name = "CREATION_DATE")
    private java.sql.Date creationDate;
    @Column(name = "DELETE_YN")
    private char deleteYn;
    @Column(name = "MODIFIED_DATE")
    private java.sql.Date modifiedDate;
    @Column(name = "RESOURCE_CODE")
    private int resourceCode;
}

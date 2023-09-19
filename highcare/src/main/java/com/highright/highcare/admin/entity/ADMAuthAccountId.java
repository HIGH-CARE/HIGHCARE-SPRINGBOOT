package com.highright.highcare.admin.entity;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ADMAuthAccountId implements Serializable {

    @Column(name ="AUTH_CODE")
    private String authCode;

    @Column(name="ID")
    private String id;



    @Builder
    public ADMAuthAccountId(String authCode, String id) {
        this.authCode = authCode;
        this.id = id;
    }
}
package com.highright.highcare.admin.entity;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="TBL_AUTH_ACCOUNT")
@Getter
@Setter
public class ADMAuthAccount {

//    private static final long serialVersionUID = 1L; // 추가


    @EmbeddedId
    private ADMAuthAccountId id;

//    @Column(name ="ID")
//    private String memberId;
//
//    @Column(name = "AUTH_CODE")
//    private String authCode;

//    @OneToMany(mappedBy = "authAccount", cascade = CascadeType.REMOVE, orphanRemoval = true)
//    private List<Menu> menus;

//    public void addMenu(Menu menu) {
//        if (menus == null) {
//            menus = new ArrayList<>();
//        }
//        menus.add(menu);
//        menu.setAuthAccount(this);
//    }
//
//    public void removeMenu(Menu menu) {
//        if (menus != null) {
//            menus.remove(menu);
//            menu.setAuthAccount(null);
//        }
//    }


    @Builder
    public ADMAuthAccount(ADMAuthAccountId id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ADMAuthAccount{" +
                "id=" + id +
                '}';
    }
}
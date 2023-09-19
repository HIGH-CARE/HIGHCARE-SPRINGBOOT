package com.highright.highcare.oauth.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TBL_ACCOUNT_OAUTH")
@NoArgsConstructor
@Getter
@ToString
public class OAuthUser {

    @Id
    @Column(name="OAUTH_ID")
    private String oauthId;
    @Column(name="ID")
    private String id;
    @Column(name="PROVIDER")
    private String provider;
    @Column(name="PROVIDE_EMAIL")
    private String provideEmail;
    @Column(name="PROVIDE_NICKNAME")
    private String provideNickname;
    @Column(name="PROVIDE_NAME")
    private String provideName;

    @Builder
    public OAuthUser(String oauthId, String id, String provider, String provideEmail, String provideNickname, String provideName) {
        this.oauthId = oauthId;
        this.id = id;
        this.provider = provider;
        this.provideEmail = provideEmail;
        this.provideNickname = provideNickname;
        this.provideName = provideName;
    }
}

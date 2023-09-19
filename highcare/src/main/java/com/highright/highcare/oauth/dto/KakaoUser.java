package com.highright.highcare.oauth.dto;

import java.util.Map;

public class KakaoUser implements OAuthUserInfo {

    private Map<String, Object> attribute;

    public KakaoUser(Map<String, Object> attribute){
        this.attribute = attribute;
    }
    @Override
    public String getProviderId() {

        return (String)attribute.get("providerId");
    }

    @Override
    public String getProvider() {
        return (String) attribute.get("provider");
    }

    @Override
    public String getEmail() {
        return (String) attribute.get("email");
    }

    @Override
    public String getName() {
        return (String)attribute.get("name");
    }

    @Override
    public String getId() {
        return (String)attribute.get("id");
    }
}

package com.highright.highcare.oauth.repository;

import com.highright.highcare.oauth.entity.OAuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthRepository extends JpaRepository<OAuthUser, String> {
}

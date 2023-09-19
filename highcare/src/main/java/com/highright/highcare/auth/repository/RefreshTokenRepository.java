package com.highright.highcare.auth.repository;

import com.highright.highcare.auth.entity.AUTHRefreshToken;
import org.springframework.data.repository.CrudRepository;


public interface RefreshTokenRepository extends CrudRepository<AUTHRefreshToken, String> {


}

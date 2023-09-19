package com.highright.highcare.auth.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@Setter
@Getter
@RedisHash(value="id", timeToLive = 36000)     // 10시간 test
@ToString
@Builder
public class AUTHRefreshToken {

    @Id
    private String id;
    private String refreshToken;
}

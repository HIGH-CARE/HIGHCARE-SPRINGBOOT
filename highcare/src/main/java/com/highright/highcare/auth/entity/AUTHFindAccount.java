package com.highright.highcare.auth.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@Setter
@Getter
@RedisHash(value="id", timeToLive = 180)     // 3minutes
@ToString
@Builder
public class AUTHFindAccount {

    @Id
    private String id;               // phone number or email address
    private String authCode;      // 인증코드
}

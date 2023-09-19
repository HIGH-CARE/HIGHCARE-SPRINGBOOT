package com.highright.highcare.chatting.controller;


import com.highright.highcare.chatting.dto.Conversation;
import com.highright.highcare.chatting.dto.MessageModel;
import com.highright.highcare.chatting.dto.User;
import com.highright.highcare.chatting.service.UserService;
import com.highright.highcare.chatting.storage.UserStorage;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsersController {
    private static final Logger logger = LoggerFactory.getLogger(UsersController.class);

    @Autowired
    private RedisTemplate<String, Conversation> conversationTemplate;

    @Autowired
    private UserStorage userStorage;

    @Autowired
    private UserService userService;

    // 빈 생성과 동시에 Storage에 DB데이터 add
    @Operation(summary = "데이터 동기화", description = "빈 생성과 동시에 Storage에 유저정보 DB데이터를 추가합니다.", tags = {"UsersController"})
    @PostConstruct
    public void init() {
        userService.syncUsersFromDatabase();
    }

    @Operation(summary = "사용자 검색 및 대화 데이터 초기화", description = "사용자 검색 후, 대화 데이터가 초기화되지 않은 경우 대화 데이터를 초기화합니다.", tags = {"UsersController"})
    public ResponseEntity<Integer> search(@PathVariable String userId, @PathVariable String myId){
        System.out.println("myId=============>" + myId + " userId============> " + userId);
        System.out.println("userStorage =============> " + userStorage);

        if(!userStorage.userExists(userId)){
            return new ResponseEntity<>(101, HttpStatus.OK); // 존재하지 않는 사용자
        }


        HashOperations<String, String, Conversation> hashOperations = conversationTemplate.opsForHash();

        if(hashOperations.hasKey(myId, userId)){
            return new ResponseEntity<>(102, HttpStatus.OK);    // 이미 추가된 사용자
        }
        System.out.println("UsersController /search/{myId}/{userId} ====================> " + userId);
        hashOperations.put(myId, userId, new Conversation(userId, new ArrayList<>()));
        return new ResponseEntity<>(100, HttpStatus.OK);    //  성공

    }

    @Operation(summary = "채팅방목록 불러오기", description = "유저들의 채팅방 목록을 로드합니다.", tags = {"UsersController"})
    @GetMapping("/fetchAllUsers/{empName}")
    public ResponseEntity<Collection<Conversation>> fetchAll(@PathVariable String empName){

        HashOperations<String, String, Conversation> hashOperations = conversationTemplate.opsForHash();
        logger.info("empName: {}", empName);
        List<Conversation> users = hashOperations.values(empName);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    @Operation(summary = "채팅방 나가기", description = "채팅방을 영구적으로 나갑니다. 채팅방과 모든 대화기록도 삭제됩니다.", tags = {"UsersController"})
    @DeleteMapping("/leaveChat/{author}/{to}")
    public ResponseEntity<Boolean> leaveChat(@PathVariable String author, @PathVariable String to) {
        HashOperations<String, String, Conversation> hashOperations = conversationTemplate.opsForHash();
        logger.info("Leave chat host: {}, partner: {}", author, to);
        hashOperations.delete(author, to);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}


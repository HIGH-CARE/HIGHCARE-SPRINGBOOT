package com.highright.highcare.chatting.service;

import com.highright.highcare.admin.entity.ADMAccount;
import com.highright.highcare.chatting.Repository.UserRepository;
import com.highright.highcare.chatting.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserStorage userStorage;

    public void syncUsersFromDatabase() {
        List<ADMAccount> usersFromDatabase = userRepository.findAll(); // 오라클에서 모든 사용자 가져오기
        for (ADMAccount user : usersFromDatabase) {
            userStorage.addUser(user.getMemberId(), user.getEmployee().getName()); // UserStorage에 사용자 아이디 추가
//            userStorage.addUser(user.getEmployee().getName()); // UserStorage에 사용자 이름 추가
            System.out.println("user ===============>" + user);
        }

        System.out.println("usersFromDatabase===============>" + usersFromDatabase);
    }

}

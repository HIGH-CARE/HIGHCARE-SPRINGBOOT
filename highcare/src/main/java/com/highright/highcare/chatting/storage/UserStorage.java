package com.highright.highcare.chatting.storage;

import lombok.ToString;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@ToString
public class UserStorage {
    private static UserStorage instance;
    private Map<String, String> userIdToNameMap;

    private UserStorage() {
        userIdToNameMap = new HashMap<>();
    }

    public static synchronized UserStorage getInstance() {
        if (instance == null) {
            instance = new UserStorage();
        }
        return instance;
    }

    public Map<String, String> getUserIdToNameMap() {
        return userIdToNameMap;
    }

    public boolean userExists(String userId) {
        return userIdToNameMap.containsKey(userId);
    }

    public void addUser(String userId, String userName) {
        userIdToNameMap.put(userId, userName);
        System.out.println("Added user====================> " + userName);
    }

    public void removeUser(String userId) {
        userIdToNameMap.remove(userId);
    }

    public String getUserName(String userId) {
        return userIdToNameMap.get(userId);
    }
}
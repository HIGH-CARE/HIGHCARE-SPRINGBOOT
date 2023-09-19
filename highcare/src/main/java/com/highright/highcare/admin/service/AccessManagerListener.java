package com.highright.highcare.admin.service;

import com.highright.highcare.admin.entity.ADMAuthAccount;
import com.highright.highcare.admin.entity.AccessManager;
import com.highright.highcare.auth.entity.AUTHAuthAccount;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PostUpdate;

@Component
public class AccessManagerListener {

    @PersistenceContext
    private EntityManager entityManager;

    @PostUpdate
    public void onAccessManagerUpdate(AccessManager accessManager) {
        // accessManager의 상태 변경 후 실행될 로직
        AUTHAuthAccount authAccount = entityManager.find(AUTHAuthAccount.class, accessManager.getId());
        if (authAccount != null) {
            // accessManager의 상태에 따라 계정상태 유형 설정
            String authCode = calculateAccountStatus(accessManager);
            authAccount.setAuthCode(authCode);
            entityManager.merge(authAccount);
        }
    }

    private String calculateAccountStatus(AccessManager accessManager) {
        // accessManager의 상태에 따라 계정상태 유형 계산 로직을 구현
        // 예를 들어, "Y" 상태에 따라 다른 계산을 수행할 수 있습니다.
        if ("Y".equals(accessManager.getIsLock())) {
            return "ROLE_PRE_USER";
        } else if ("Y".equals(accessManager.getIsInActive())) {
            return "ROLE_PRE_USER";
        } else if ("Y".equals(accessManager.getIsExpired())) {
            return "ROLE_DRAW_USER";
        } else if ("Y".equals(accessManager.getIsWithDraw())) {
            return "ROLE_DRAW_USER";
        } else if ("N".equals(accessManager.getIsLock())
                && "N".equals(accessManager.getIsInActive())
                && "N".equals(accessManager.getIsExpired())
                && "N".equals(accessManager.getIsWithDraw())) {
            return "ROLE_USER";
        }
        else {
            return "ROLE_USER"; // 다른 상황에 따른 계정상태 유형 설정
        }
    }
}

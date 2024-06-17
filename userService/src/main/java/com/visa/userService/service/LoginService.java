package com.visa.userService.service;


import com.visa.lib.entity.Auth.LoginAttempt;
import com.visa.lib.entity.Auth.UserAccount;
import com.visa.userService.model.dto.LoginAttemptResponse;
import jakarta.transaction.Transactional;

public interface LoginService {
    @Transactional
    void addLoginAttempt(UserAccount user);


    void resetFailedAttempts(final String key, String  username);


    boolean unlockWhenTimeExpired(LoginAttempt attempt);

    LoginAttemptResponse findRecentLoginAttempts(String email);

    void loginFailed(final String key, String username);

    boolean isBlocked(String username);
}

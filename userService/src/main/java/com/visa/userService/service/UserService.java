package com.visa.userService.service;

import com.visa.lib.entity.auth.UserAccount;
import com.visa.userService.model.dto.SignupRequest;

import java.util.Optional;

public interface UserService {
    void signup(SignupRequest user);
    Optional<UserAccount> findById(Integer userId);
}

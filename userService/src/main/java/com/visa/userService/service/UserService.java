package com.visa.userService.service;


import com.visa.lib.entity.Auth.UserAccount;
import com.visa.userService.model.dto.ChangePasswordRequest;
import com.visa.userService.model.dto.SignupRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

public interface UserService {
    UserAccount signup(SignupRequest user);
    Optional<UserAccount> findById(Integer userId);

    UserAccount loadUserByUsername(String username);

    void logout();

    @Transactional
    String changePassword(ChangePasswordRequest request);
}

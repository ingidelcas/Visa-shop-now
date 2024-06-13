package com.visa.userService.service.impl;



import java.time.LocalDateTime;
import java.util.List;

import com.visa.lib.entity.auth.LoginAttempt;
import com.visa.lib.entity.auth.UserAccount;
import com.visa.lib.repository.auth.LoginAttemptRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LoginService implements com.visa.userService.service.LoginService {

  private final LoginAttemptRepository repository;
  private final ModelMapper modelMapper;

  public LoginService(LoginAttemptRepository repository, ModelMapper modelMapper) {
    this.repository = repository;
    this.modelMapper = modelMapper;
  }

  @Transactional
  public void addLoginAttempt(String username, boolean success) {
    LoginAttempt loginAttempt = LoginAttempt.builder()
            .username(username)
            .success(success)
            .createdAt(LocalDateTime.now())
            .build();
    repository.save(loginAttempt);
  }

  public List<LoginAttempt> findRecentLoginAttempts(String email) {
    return repository.findRecent(email);
  }
}

package com.visa.userService.service.impl;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.visa.lib.Utils.Constant;
import com.visa.lib.entity.Auth.LoginAttempt;
import com.visa.lib.entity.Auth.UserAccount;
import com.visa.lib.repository.auth.LoginAttemptRepository;
import com.visa.lib.repository.auth.UserAccountRepository;
import com.visa.userService.exceptions.LockedException;
import com.visa.userService.exceptions.NotFoundException;
import com.visa.userService.model.dto.LoginAttemptResponse;
import com.visa.userService.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    private final LoginAttemptRepository repository;
    private final UserAccountRepository accountRepository;
    private final ModelMapper modelMapper;

    private LoadingCache<String, Integer> attemptsCache;


    private final HttpServletRequest request;

    public LoginServiceImpl(LoginAttemptRepository repository, UserAccountRepository accountRepository, ModelMapper modelMapper, HttpServletRequest request) {
        this.repository = repository;
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
        this.request = request;
        attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.DAYS).build(new CacheLoader<String, Integer>() {
            @Override
            public Integer load(final String key) {
                return 0;
            }
        });

    }

    @Override
    @Transactional
    public void addLoginAttempt(UserAccount user) {
        LoginAttempt loginAttempt = LoginAttempt.builder()
                .user(user)
                .failedAttempt(0)
                .accountNonLocked(true)
                .build();
        repository.save(loginAttempt);
    }

    public void increaseFailedAttempts(UserAccount  user) {
        int newFailAttempts = user.getLoginAttempt().getFailedAttempt() + 1;
        user.getLoginAttempt().setFailedAttempt(newFailAttempts);
        accountRepository.save(user);
    }

    @Override
    public void resetFailedAttempts(final String key, String username) {

        UserAccount user = accountRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User not found with username: " + username));
        attemptsCache.put(key, 0);
        user.setEnabled(true);
        user.getLoginAttempt().setFailedAttempt(0);
        user.getLoginAttempt().setAccountNonLocked(true);
        accountRepository.save(user);
    }


    public void lock(UserAccount user) {
        user.setEnabled(false);
        user.getLoginAttempt().setAccountNonLocked(false);
        user.getLoginAttempt().setLockTime(new Date());
        accountRepository.save(user);
    }

    @Override
    public boolean unlockWhenTimeExpired(LoginAttempt attempt) {
        long lockTimeInMillis = attempt.getLockTime().getTime();
        long currentTimeInMillis = System.currentTimeMillis();

        if (lockTimeInMillis + Constant.LOCK_TIME_DURATION < currentTimeInMillis) {
            attempt.setAccountNonLocked(true);
            attempt.setLockTime(null);
            attempt.setFailedAttempt(0);
            repository.save(attempt);
            return true;
        }

        return false;
    }

    @Override
    public LoginAttemptResponse findRecentLoginAttempts(String email) {
        return modelMapper.map(repository.findRecent(email), LoginAttemptResponse.class);
    }


    public void loginFailed(final String key, String username) {
        int attempts;
        try {
            attempts = attemptsCache.get(key);
        } catch (final ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
        UserAccount user = accountRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User not found with username: " + username));

        if (user.isEnabled() && user.getLoginAttempt().getAccountNonLocked()) {
            if (user.getLoginAttempt().getFailedAttempt() < Constant.MAX_FAILED_ATTEMPTS - 1) {
                increaseFailedAttempts(user);
            } else {
                lock(user);

                throw new LockedException("Your account has been locked due to 3 failed attempts."
                        + " It will be unlocked after 24 hours.");
            }
        } else if (!user.getLoginAttempt().getAccountNonLocked()) {
            if (unlockWhenTimeExpired(user.getLoginAttempt())) {
                throw new LockedException("Your account has been unlocked. Please try to login again.");
            }
        }


    }

    public boolean isBlocked(String username) {

        UserAccount user = accountRepository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User not found with username: " + username));
        if (!user.getLoginAttempt().getAccountNonLocked())
            return true;

        try {
            return attemptsCache.get(getClientIP()) >= Constant.MAX_FAILED_ATTEMPTS;
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    private String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader != null) {
            return xfHeader.split(",")[0];
        }
        return request.getRemoteAddr();
    }
}

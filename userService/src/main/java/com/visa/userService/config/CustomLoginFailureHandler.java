package com.visa.userService.config;

import com.visa.userService.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Component
public class CustomLoginFailureHandler implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginService loginAttemptService;


    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent e) {

        String username = e.getAuthentication().getName();
        if(loginAttemptService.isBlocked(username)){
            throw new LockedException("User blocked: ");
        }
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
            loginAttemptService.loginFailed(request.getRemoteAddr(), username);
        } else {
            loginAttemptService.loginFailed(xfHeader.split(",")[0], username);
        }
    }
}

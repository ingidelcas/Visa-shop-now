package com.visa.productService.security;


import com.visa.lib.entity.Auth.UserAccount;
import com.visa.lib.repository.auth.UserAccountRepository;
import com.visa.productService.exception.NotFoundException;
import com.visa.productService.feignclient.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserClient userClient;

    private final UserAccountRepository repository;

    public UserDetailsServiceImpl(UserAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) {
        Collection<String> mappedAuthorities = new ArrayList<>();
        UserAccount user = userClient.getUserByName(userName);
        user.getRoles().forEach(rol -> mappedAuthorities.add(rol.getName().name()));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .authorities(mappedAuthorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()))
                .build();
    }
}

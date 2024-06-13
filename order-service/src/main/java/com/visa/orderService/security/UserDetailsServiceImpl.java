package com.visa.orderService.security;

import com.visa.lib.entity.auth.UserAccount;
import com.visa.lib.exceptions.NotFoundException;
import com.visa.lib.repository.auth.UserAccountRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserAccountRepository repository;

    public UserDetailsServiceImpl(UserAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) {
        Collection<String> mappedAuthorities = new ArrayList<>();

        UserAccount user = repository.findByUsername(userName).orElseThrow(() ->
                new NotFoundException(String.format("User does not exist, user name: %s", userName)));
        user.getRoles().forEach(rol -> mappedAuthorities.add(rol.getName().name()));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserName())
                .password(user.getPassword())
                .authorities(mappedAuthorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()))
                .build();
    }
}

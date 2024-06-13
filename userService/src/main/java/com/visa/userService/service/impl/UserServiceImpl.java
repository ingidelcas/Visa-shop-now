package com.visa.userService.service.impl;


import com.visa.lib.entity.auth.Role;
import com.visa.lib.entity.auth.RoleName;
import com.visa.lib.entity.auth.UserAccount;
import com.visa.lib.exceptions.DuplicateException;
import com.visa.lib.exceptions.NotFoundException;
import com.visa.lib.repository.auth.UserAccountRepository;
import com.visa.userService.model.dto.SignupRequest;
import com.visa.userService.service.RoleService;
import com.visa.userService.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final RoleService roleService;

    public UserServiceImpl(UserAccountRepository repository, PasswordEncoder passwordEncoder, ModelMapper modelMapper, RoleService roleService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.roleService = roleService;
    }

    @Transactional
    @Override
    public void signup(SignupRequest user) {
        String email = user.getPerson().getEmail();
        Optional<UserAccount> existingUser = repository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new DuplicateException(String.format("User with the username address '%s' already exists.", email));
        }

        UserAccount userAccount = modelMapper.map(user, UserAccount.class);
        userAccount.setPassword(passwordEncoder.encode(user.getPassword()));
        userAccount.setRoles(user.getRoles()
                .stream().map(role -> {
                    return roleService.findByName((mapToRoleName(role)))
                            .orElseThrow(() -> new RuntimeException("Role not found in the database."));
                })
                .collect(Collectors.toSet()));
        repository.save(userAccount);
    }

    @Override
    public Optional<UserAccount> findById(Integer userId) {
        return Optional.of(repository.findById(userId))
                .orElseThrow(() -> new NotFoundException("User not found with userId: " + userId));
    }


    private Set<Role> getRole(Set<Role> roles) {
        return roles.stream()
                .map(role -> {
                    return roleService.findByName(mapToRoleName(role.getName().name()))
                            .orElseThrow(() -> new RuntimeException("Role not found in the database."));
                })
                .collect(Collectors.toSet());
    }


    private RoleName mapToRoleName(String roleName) {
        return switch (roleName) {
            case "ADMIN", "admin", "Admin" -> RoleName.ADMIN;
            case "PM", "pm", "Pm" -> RoleName.PM;
            case "USER", "user", "User" -> RoleName.USER;
            default -> null;
        };
    }

}

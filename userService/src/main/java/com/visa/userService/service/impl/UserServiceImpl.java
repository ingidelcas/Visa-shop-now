package com.visa.userService.service.impl;


import com.visa.lib.entity.Auth.Role;
import com.visa.lib.entity.Auth.RoleName;
import com.visa.lib.entity.Auth.UserAccount;
import com.visa.lib.exceptions.AccessDeniedException;
import com.visa.lib.repository.auth.UserAccountRepository;
import com.visa.userService.exceptions.DuplicateException;
import com.visa.userService.exceptions.NotFoundException;
import com.visa.userService.helper.JwtHelper;
import com.visa.userService.model.dto.ChangePasswordRequest;
import com.visa.userService.model.dto.SignupRequest;
import com.visa.userService.service.RoleService;
import com.visa.userService.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
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
    public UserAccount signup(SignupRequest user) {
        String email = user.getPerson().getEmail();
        Optional<UserAccount> existingUser = repository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new DuplicateException(String.format("User with the username address '%s' already exists.", email));
        }

        UserAccount userAccount = modelMapper.map(user, UserAccount.class);
        userAccount.setPassword(passwordEncoder.encode(user.getPassword()));
        userAccount.setEnabled(true);
        userAccount.setRoles(user.getRoles()
                .stream().map(role -> {
                    return roleService.findByName((mapToRoleName(role)))
                            .orElseThrow(() -> new RuntimeException("Role not found in the database."));
                })
                .collect(Collectors.toSet()));
        return repository.save(userAccount);
    }

    @Override
    public Optional<UserAccount> findById(Integer userId) {
        return Optional.of(repository.findById(userId))
                .orElseThrow(() -> new NotFoundException("User not found with userId: " + userId));
    }

    @Override
    public UserAccount loadUserByUsername(String username) {
        UserAccount user = repository.findByUsername(username).orElseThrow(
                () -> new NotFoundException("User not found with username: " + username)
        );/**/
        return user;

    }

    @Override
    public void logout() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityContextHolder.getContext().setAuthentication(null);
        String currentToken = getCurrentToken();
        if (authentication != null && authentication.isAuthenticated()) {
            // Invalidate the current token by reducing its expiration time
            String updatedToken = JwtHelper.reduceTokenExpiration(currentToken);
        }
        SecurityContextHolder.clearContext();

    }

    @Transactional
    @Override
    public String changePassword(ChangePasswordRequest request) {
        try {
            UserDetails userDetails = getCurrentUserDetails();
            String username = userDetails.getUsername();

            UserAccount existingUser = repository.findByUsername(username)
                    .orElseThrow(() -> new NotFoundException("User not found with username " + username));

            if (passwordEncoder.matches(request.oldPassword(), userDetails.getPassword())) {
                if (validateNewPassword(request.newPassword(), request.confirmPassword())) {
                    existingUser.setPassword(passwordEncoder.encode(request.newPassword()));
                    repository.save(existingUser);

                    return "Password changed successfully";
                }

                return "Password changed failed.";
            } else {
                throw new NotFoundException("Incorrect password");
            }
        } catch (Exception e) {
            throw new AccessDeniedException("Transaction silently rolled back");
        }
    }

    private UserDetails getCurrentUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof UserDetails) {
            return (UserDetails) authentication.getPrincipal();
        } else {
            throw new AccessDeniedException("User not authenticated.");
        }
    }

    private boolean validateNewPassword(String newPassword, String confirmPassword) {
        return Objects.equals(newPassword, confirmPassword);
    }

    private String getCurrentToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object credentials = authentication.getCredentials();

            if (credentials instanceof String) {
                return (String) credentials;
            }
        }

        return null;
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

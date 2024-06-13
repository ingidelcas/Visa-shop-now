package com.visa.userService.service.impl;

import com.visa.lib.entity.auth.Role;
import com.visa.lib.entity.auth.RoleName;
import com.visa.lib.entity.auth.UserAccount;
import com.visa.lib.exceptions.NotFoundException;
import com.visa.lib.repository.auth.RoleRepository;
import com.visa.lib.repository.auth.UserAccountRepository;
import com.visa.userService.service.RoleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserAccountRepository userRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository, UserAccountRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Optional<Role> findByName(RoleName name)  {
        try {
            return Optional.ofNullable(roleRepository.findByName(name)
                    .orElseThrow(() -> new RoleNotFoundException("Role Not Found with name: " + name)));
        } catch (RoleNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public boolean assignRole(Long userId, String roleName) {
        UserAccount user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

        Role role = null;
        try {
            role = roleRepository.findByName(mapToRoleName(roleName))
                    .orElseThrow(() -> new RoleNotFoundException("Role not found in system: " + roleName));
        } catch (RoleNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (user.getRoles().contains(role))
            return false;

        user.getRoles().add(role);
        userRepository.save(user);
        return true;
    }

    @Transactional
    @Override
    public boolean revokeRole(Long id, String roleName) {
        UserAccount user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found."));

        if (user.getRoles().removeIf(role -> role.getName().equals(mapToRoleName(roleName)))) {
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public List<String> getUserRoles(Long id) {
        UserAccount user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found."));

        List<String> roleNames = new ArrayList<>();
        user.getRoles().forEach(userRole -> roleNames.add(userRole.getName().toString()));
        return roleNames;
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

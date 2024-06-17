package com.visa.userService.service;




import com.visa.lib.entity.Auth.Role;
import com.visa.lib.entity.Auth.RoleName;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(RoleName name);

    boolean assignRole(Long id, String roleName);

    boolean revokeRole(Long id, String roleName);

    List<String> getUserRoles(Long id);
}

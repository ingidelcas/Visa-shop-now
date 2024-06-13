package com.visa.lib.repository.auth;

import com.visa.lib.entity.auth.Role;
import com.visa.lib.entity.auth.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {

    @Query("SELECT r FROM Role r WHERE r.name = :name")
    Optional<Role> findByName(@Param("name") RoleName name);

    @Query("SELECT u.roles FROM UserAccount u WHERE u.userId = :id")
    List<Role> findByUserId(@Param("id") Long id);
}

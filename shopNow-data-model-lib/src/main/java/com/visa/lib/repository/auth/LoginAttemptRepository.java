package com.visa.lib.repository.auth;

import com.visa.lib.entity.auth.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM shopnow.login_attempt WHERE username = :username ORDER BY created_at DESC LIMIT  10")
    public List<LoginAttempt> findRecent(@Param("username")String username);

}

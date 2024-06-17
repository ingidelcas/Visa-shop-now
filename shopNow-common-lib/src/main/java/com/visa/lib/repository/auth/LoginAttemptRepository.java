package com.visa.lib.repository.auth;


import com.visa.lib.entity.Auth.LoginAttempt;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Integer> {

    @Query(nativeQuery = true, value = "SELECT * FROM shopnow.login_attempt WHERE username = :username ORDER BY created_at DESC LIMIT  10")
    public List<LoginAttempt> findRecent(@Param("username") String username);

    @Query(nativeQuery = true,
            value = "UPDATE shopnow.login_attempt SET failed_attempt = ?1 WHERE user_id = ?2")
    public void updateFailedAttempts(int failAttempts, Integer user_id);

}

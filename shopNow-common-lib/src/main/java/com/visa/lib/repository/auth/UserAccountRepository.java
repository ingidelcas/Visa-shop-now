package com.visa.lib.repository.auth;


import com.visa.lib.entity.Auth.Person;
import com.visa.lib.entity.Auth.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {

    @Query("SELECT u FROM UserAccount u WHERE u.id = :id")
    Optional<UserAccount> findById(@Param("id") Long id);

    @Query("SELECT u FROM UserAccount u WHERE u.userName = :username") // JPQL
    Optional<UserAccount> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM UserAccount u WHERE u.person.email = :email")
    Optional<UserAccount> findByEmail(@Param("email") String name);

    @Query("SELECT u FROM UserAccount u WHERE u.person = :person")
    Optional<UserAccount>findByPerson(@Param("person") Person person);

    @Query("SELECT CASE WHEN COUNT(u) > 0 " +
            "THEN true " +
            "ELSE false " +
            "END FROM UserAccount u " +
            "WHERE u.userName = :username")
    Boolean existsByUsername(@Param("username") String username);

    @Query("SELECT CASE WHEN COUNT(u) > 0 " +
            "THEN true " +
            "ELSE false " +
            "END FROM UserAccount u WHERE u.person.email = :email")
    Boolean existsByEmail(@Param("email") String email);

}

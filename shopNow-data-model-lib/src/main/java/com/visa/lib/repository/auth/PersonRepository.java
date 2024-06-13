package com.visa.lib.repository.auth;

import com.visa.lib.entity.auth.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person,Integer> {

}

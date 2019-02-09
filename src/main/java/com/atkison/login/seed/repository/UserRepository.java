package com.atkison.login.seed.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.atkison.login.seed.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}

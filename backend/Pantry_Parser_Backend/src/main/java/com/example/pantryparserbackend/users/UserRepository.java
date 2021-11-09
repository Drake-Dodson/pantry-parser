package com.example.pantryparserbackend.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Basic repository for users
 */
public interface UserRepository extends JpaRepository<User, Long> {
    User findById(int id);
    User findByEmail(String email);
}

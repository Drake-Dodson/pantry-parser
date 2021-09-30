package com.example.pantryparserbackend.users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findById(int id);
    User findByEmail(String email);
}

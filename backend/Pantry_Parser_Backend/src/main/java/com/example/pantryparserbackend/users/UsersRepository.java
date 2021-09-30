package com.example.pantryparserbackend.users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findById(int id);
    Users findByEmail(String email);
}

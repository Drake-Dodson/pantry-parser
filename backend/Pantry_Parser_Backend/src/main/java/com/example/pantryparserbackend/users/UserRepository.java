package com.example.pantryparserbackend.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * Basic repository for users
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findById(int id);
    User findByEmail(String email);
}

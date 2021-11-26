package com.example.pantryparserbackend.users;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Basic repository for users
 */
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    User findById(int id);
    User findByEmail(String email);
}

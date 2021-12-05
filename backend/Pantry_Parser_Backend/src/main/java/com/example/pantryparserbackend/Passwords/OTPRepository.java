package com.example.pantryparserbackend.Passwords;

import com.example.pantryparserbackend.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * basic repository for one time passwords
 */
public interface OTPRepository extends JpaRepository<OTP, Long> {
    OTP findById(int id);
    @Transactional
    void deleteById(int id);

    @Query(value = "SELECT o FROM OTP o WHERE " +
            "o.user = ?1")
    List<OTP> findByUser(User user);
}

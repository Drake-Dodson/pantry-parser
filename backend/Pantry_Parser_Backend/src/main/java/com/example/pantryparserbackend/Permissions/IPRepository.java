package com.example.pantryparserbackend.Permissions;

import com.example.pantryparserbackend.Permissions.IP;
import com.example.pantryparserbackend.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * basic repository for one time passwords
 */
public interface IPRepository extends JpaRepository<IP, Long> {
    IP findById(int id);
    @Transactional
    void deleteById(int id);

    @Query(value = "SELECT i FROM IP i WHERE " +
            "i.user = ?1")
    List<IP> findByUser(User user);

    @Query(value = "SELECT i FROM IP i WHERE " +
            "i.ip = ?1")
    IP findByIP(String ip);
}

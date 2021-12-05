package com.example.pantryparserbackend.Permissions;

import com.example.pantryparserbackend.Permissions.IP;
import com.example.pantryparserbackend.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    /**
     * Finds the IPs associated with a user
     * @param user input user
     * @return list of IPs
     */
    @Query(value = "SELECT i FROM IP i WHERE i.user = ?1")
    List<IP> findByUser(User user);

    /**
     * Finds an IP object by an ip address
     * @param ip input address
     * @return IP object
     */
    @Query(value = "SELECT i FROM IP i WHERE i.ip = ?1")
    IP findByIP(String ip);

    /**
     * Deletes an IP by its address
     * @param ip input address
     */
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM IP i WHERE i.ip = ?1")
    void deleteByIP(String ip);
}

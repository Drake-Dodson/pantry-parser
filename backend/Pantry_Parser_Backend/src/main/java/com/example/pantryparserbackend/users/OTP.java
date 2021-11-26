package com.example.pantryparserbackend.users;

import com.example.pantryparserbackend.Util.PasswordUtil;
import lombok.Getter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Entity
@Table(name = "one_time_passwords")
public class OTP {
    @Id
    @Getter
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter
    @Column(nullable = false)
    private String hash;

    @Getter
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime created_date;

    @Getter
    @ManyToOne
    private User user;

    public OTP(){}

    public OTP(String password, User user) {
        this.hash = PasswordUtil.newHash(password);
        this.user = user;
        this.created_date = LocalDateTime.now();
    }

    public boolean outOfDate() {
        return ChronoUnit.MINUTES.between(this.created_date, LocalDateTime.now()) <= 10;
    }

    public boolean verify(String password) {
        return PasswordUtil.comparePasswords(password, this.hash) && !this.outOfDate();
    }
}

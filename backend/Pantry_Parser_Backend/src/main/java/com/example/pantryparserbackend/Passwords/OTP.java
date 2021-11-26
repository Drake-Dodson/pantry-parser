package com.example.pantryparserbackend.Passwords;

import com.example.pantryparserbackend.Util.PasswordUtil;
import com.example.pantryparserbackend.users.User;
import lombok.Getter;

import javax.persistence.*;
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
    private Date created_date;

    @Getter
    @ManyToOne
    private User user;

    public OTP(){}

    public OTP(String password, User user) {
        this.hash = PasswordUtil.newHash(password);
        this.user = user;
        this.created_date = new Date();
    }

    public boolean outOfDate() {
        return this.created_date.after(new Date(this.created_date.getTime() + (10 * 60 * 1000)));
    }

    public boolean verify(String password) {
        return PasswordUtil.comparePasswords(password, this.hash);
    }
}

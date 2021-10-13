package com.example.pantryparserbackend;

import com.example.pantryparserbackend.users.User;
import com.example.pantryparserbackend.users.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class PantryParserBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PantryParserBackendApplication.class, args);
    }
}

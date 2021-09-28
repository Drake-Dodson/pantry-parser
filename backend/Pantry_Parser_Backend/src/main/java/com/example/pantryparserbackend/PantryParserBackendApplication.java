package com.example.pantryparserbackend;

import com.example.pantryparserbackend.users.Users;
import com.example.pantryparserbackend.users.UsersRepository;
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

    @Bean
    CommandLineRunner initUser(UsersRepository usersRepository) {
        return args -> {
            Users user1 = new Users("John", "john@somemail.com");
            Users user2 = new Users("Jane", "jane@somemail.com");
            Users user3 = new Users("Justin", "justin@somemail.com");
            usersRepository.save(user1);
            usersRepository.save(user2);
            usersRepository.save(user3);
        };
    }


}

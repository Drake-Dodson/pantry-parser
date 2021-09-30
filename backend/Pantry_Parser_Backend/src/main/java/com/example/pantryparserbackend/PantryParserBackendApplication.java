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

    @Bean
    CommandLineRunner initUser(UserRepository userRepository) {
        // These will be saved to the database everytime it's started and is only really for testing purposes so it may need
        // to be taken out.
        return args -> {
            User user1 = new User("John", "john@somemail.com");
            User user2 = new User("Jane", "jane@somemail.com");
            User user3 = new User("Justin", "justin@somemail.com");
            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);
        };
    }
}

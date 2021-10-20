package com.example.pantryparserbackend.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String welcome() {
        return "Pantry Parser Super Cool Homepage";
    }

    @GetMapping(path = "/user/{id}")
    public User getUserById(@PathVariable int id)
    {
        return userRepository.findById(id);
    }

    @GetMapping(path = "/user/email")
    public User getUserByEmail(@RequestParam String email)
    {
        return userRepository.findByEmail(email);
    }

    @PostMapping(path = "/user")
    String createUser(@RequestBody User users){

        if (users == null)
            return failure;
        userRepository.save(users);
        return success;
    }

    @PostMapping(path = "/login")
    public String login(@RequestBody Login login){
        if (login == null)
            return failure;
        User actual = userRepository.findByEmail(login.email);
        if (actual == null)
            // Email not found
            return failure;
        if(actual.authenticate(login.password)){
            return success;
        }else {
            // Password incorrect
            return failure;
        }
    }
}

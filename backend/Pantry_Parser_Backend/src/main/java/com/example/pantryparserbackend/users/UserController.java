package com.example.pantryparserbackend.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.pantryparserbackend.Util.MessageUtil;

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

    @GetMapping(path = "/user/email/{email}")
    public User getUserByEmail(@PathVariable String email) throws Exception
    {
        return userRepository.findByEmail(email);
    }

    @PostMapping(path = "/user")
    String createUser(@RequestBody User users){
        if (users == null)
            return MessageUtil.newResponseMessage(false, "User was null");

        try {
            userRepository.save(users);
        }
        catch(Exception ex) {
            return MessageUtil.newResponseMessage(false, "Email already used");
        }

        return MessageUtil.newResponseMessage(true, "User created");
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

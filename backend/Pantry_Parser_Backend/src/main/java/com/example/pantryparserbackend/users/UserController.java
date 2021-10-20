package com.example.pantryparserbackend.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.pantryparserbackend.Util.MessageUtil;

// No idea if this is the correct way to do this...
class UserException extends Exception {
    public UserException(String exceptionMessage) {
        super(exceptionMessage);
    }
}

@RestController
public class UserController extends Exception {

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
    public User getUserByEmail(@RequestParam String email) throws Exception
    {
        User userInfo = userRepository.findByEmail(email);

        if(userInfo == null)
        {
            throw new UserException("Email not found");
        }

        return userInfo;
    }

    @PostMapping(path = "/user")
    String createUser(@RequestBody User users){

        // I feel like this doesn't need to be in here because it should
        // be handled by the frontend
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

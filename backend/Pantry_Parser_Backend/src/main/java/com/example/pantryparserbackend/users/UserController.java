package com.example.pantryparserbackend.users;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/")
    public String welcome() {
        return "Pantry Parser Super Cool Homepage";
    }

    @GetMapping("/user")
    public String getUsername(@RequestParam(value = "username", defaultValue = "World") String message)
    {
        return String.format("Hello, %s! You sent a get request with a parameter!", message);
    }

}

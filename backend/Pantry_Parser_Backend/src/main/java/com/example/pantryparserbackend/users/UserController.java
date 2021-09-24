package com.example.pantryparserbackend.users;

import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @GetMapping("/")
    public String welcome() {
        return "Pantry Parser Super Cool Homepage";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/user")
    public String getUsername(@RequestParam(value = "username", defaultValue = "World") String message)
    {
        return String.format("Hello, %s! You sent a get request with a parameter!", message);
    }

}

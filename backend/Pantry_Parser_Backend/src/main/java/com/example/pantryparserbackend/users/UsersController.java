package com.example.pantryparserbackend.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path="/")
public class UsersController {

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/")
    public String welcome() {
        return "Pantry Parser Super Cool Homepage";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/users/{id}")
    public Users getUserById(@PathVariable Integer id)
    {
        return usersRepository.findById(id);
    }

    @PostMapping(path = "/users")
    String createUser(@RequestBody Users users){
        if (users == null)
            return failure;
        usersRepository.save(users);
        return success;
    }
}

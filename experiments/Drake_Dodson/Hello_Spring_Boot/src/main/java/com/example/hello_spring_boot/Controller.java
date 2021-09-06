package com.example.hello_spring_boot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class Controller {

    @GetMapping("/")
    public String welcome() {
        return "Hello there";
    }

    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return "Hello there, " + name + "!";
    }
}
